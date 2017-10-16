package org.opl.allocator.trace;

import org.opl.allocator.Allocator;
import org.opl.allocator.AllocatorException;
import org.opl.util.OplUtils;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Tracks all allocated blocks as a map [address of block -> size of block}</p>
 *
 * <p>Checks that all operations are performed with a valid address that exists in the registry.</p>
 *
 * <p>Sample usage in a test:</p>
 * <pre>
 * private RegistryAllocatorProxy allocator;
 *
 *{@literal @}Before
 * public void setUp() throws Exception {
 *     this.allocator = new RegistryAllocatorProxy(new SystemAllocator());
 * }
 *
 *{@literal @}After
 * public void tearDown() throws Exception {
 *     Assert.assertEquals(0, this.allocator.getAllocatedBlocks());
 * }
 * </pre>
 *
 * <p>Can be combined with {@link DefensiveAllocatorProxy}</p>
 *
 * <p>Will not close delegate allocator - so you have to close the delegate allocator explicitly.</p>
 */
public class RegistryAllocatorProxy implements Allocator {

    private final Allocator delegate;

    private final boolean delegateIsOwned;

    private final Map<Long, Long> allocatedBlockRegistry;

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Delegate allocator instance (will not be automatically closed)
     * @param delegateIsOwned If set to <code>true</code> then delegate will also be closed on closing
     */
    public RegistryAllocatorProxy(@Nonnull Allocator delegate,
                                  boolean delegateIsOwned)
    {
        this(delegate, delegateIsOwned, new ConcurrentHashMap<>());
    }

    /**
     * Constructs a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     * @param delegateIsOwned If set to <code>true</code> then delegate will also be closed on closing
     * @param registryMap Some thread-safe map instance that should be used as a registry
     */
    public RegistryAllocatorProxy(@Nonnull Allocator delegate,
                                  boolean delegateIsOwned,
                                  @Nonnull Map<Long, Long> registryMap)
    {
        OplUtils.checkNotNull(delegate, "Delegate is not set");
        OplUtils.checkNotNull(registryMap, "Registry map is not set");

        this.delegate = delegate;
        this.delegateIsOwned = delegateIsOwned;
        this.allocatedBlockRegistry = registryMap;
    }

    public void reset() {
        this.allocatedBlockRegistry.clear();
    }

    @Override
    public void close() throws Exception {
        if (delegateIsOwned) {
            delegate.close();
        }

        long blocks = allocatedBlockRegistry.size();
        if (blocks != 0) {
            throw new IllegalStateException("There are " + blocks
                + " unreleased memory blocks. You have a memory leak.");
        }
    }

    @Override
    public long allocate(long size) throws OutOfMemoryError {
        OplUtils.checkGreaterThanZero(size, "Size must be greater than 0");

        long address = delegate.allocate(size);

        allocatedBlockRegistry.put(address, size);

        return address;
    }

    @Override
    public long reallocate(long address, long newSize) {
        OplUtils.checkGreaterThanZero(newSize, "Size must be greater than 0");

        Long v = allocatedBlockRegistry.remove(address);
        if (v == null) {
            throw new AllocatorException(
                String.format("Block [0x%016x] is not registered in the allocation registry", address));
        }

        long newAddress = delegate.reallocate(address, newSize);
        allocatedBlockRegistry.put(newAddress, newSize);

        return newAddress;
    }

    @Override
    public void free(long address) {
        Long v = allocatedBlockRegistry.remove(address);
        if (v == null) {
            throw new AllocatorException(
                String.format("Block [0x%016x] is not registered in the allocation registry", address));
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
            throw new AllocatorException(
                String.format("Block [0x%016x] is not registered in the allocation registry", address));
        }

        return v;
    }

}
