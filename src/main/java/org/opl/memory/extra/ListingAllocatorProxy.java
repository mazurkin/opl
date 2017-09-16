package org.opl.memory.extra;

import org.opl.memory.Allocator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks all allocated blocks as a map [address of block -> size of block}
 */
public class ListingAllocatorProxy implements Allocator {

    private final Allocator delegate;

    private final Map<Long, Long> allocatedBlockRegistry;

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public ListingAllocatorProxy(Allocator delegate) {
        this(delegate, new ConcurrentHashMap<>());
    }

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     * @param registryMap Some thread-safe map instance that should be used as a registry
     */
    public ListingAllocatorProxy(Allocator delegate, Map<Long, Long> registryMap) {
        this.delegate = delegate;
        this.allocatedBlockRegistry = registryMap;
    }

    @Override
    public long allocate(long size) throws OutOfMemoryError {
        long address = delegate.allocate(size);

        allocatedBlockRegistry.put(address, size);

        return address;
    }

    @Override
    public long reallocate(long address, long newSize) throws OutOfMemoryError {
        Long v = allocatedBlockRegistry.remove(address);
        if (v == null) {
            throw new IllegalStateException(String.format("Block %s is not registered in the allocation registry",
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
            throw new IllegalStateException(String.format("Block %s is not registered in the allocation registry",
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

}
