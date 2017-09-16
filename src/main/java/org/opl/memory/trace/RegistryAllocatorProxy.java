package org.opl.memory.trace;

import org.opl.memory.Allocator;
import org.opl.memory.AllocatorException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Tracks all allocated blocks as a map [address of block -> size of block}</p>
 * <p>Checks that all operations are performed with a valid address that exists in the registry.</p>
 */
public class RegistryAllocatorProxy implements Allocator {

    private final Allocator delegate;

    private final Map<Long, Long> allocatedBlockRegistry;

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public RegistryAllocatorProxy(Allocator delegate) {
        this(delegate, new ConcurrentHashMap<>());
    }

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     * @param registryMap Some thread-safe map instance that should be used as a registry
     */
    public RegistryAllocatorProxy(Allocator delegate, Map<Long, Long> registryMap) {
        this.delegate = delegate;
        this.allocatedBlockRegistry = registryMap;
    }

    @Override
    public long allocate(long size) throws OutOfMemoryError {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        long address = delegate.allocate(size);

        allocatedBlockRegistry.put(address, size);

        return address;
    }

    @Override
    public long reallocate(long address, long newSize) {
        if (newSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        Long v = allocatedBlockRegistry.remove(address);
        if (v == null) {
            throw new AllocatorException(String.format("Block %s is not registered in the allocation registry",
                Long.toHexString(address)));
        }

        long newAddress = delegate.reallocate(address, newSize);
        allocatedBlockRegistry.put(newAddress, newSize);

        return newAddress;
    }

    @Override
    public void free(long address) {
        Long v = allocatedBlockRegistry.remove(address);
        if (v == null) {
            throw new AllocatorException(String.format("Block %s is not registered in the allocation registry",
                Long.toHexString(address)));
        }

        delegate.free(address);
    }

    public Map<Long, Long> getAllocatedBlockRegistry() {
        return new HashMap<>(allocatedBlockRegistry);
    }

    public long getAllocatedBytes() {
        return allocatedBlockRegistry.values().stream()
            .mapToLong(Long::longValue)
            .sum();
    }

    public long getAllocatedBlocks() {
        return allocatedBlockRegistry.size();
    }

    public boolean hasBlock(long address) {
        return allocatedBlockRegistry.containsKey(address);
    }

    public long getBlockSize(long address) {
        Long v = allocatedBlockRegistry.get(address);
        if (v == null) {
            throw new AllocatorException(String.format("Block %s is not registered in the allocation registry",
                Long.toHexString(address)));
        }

        return v;
    }

}
