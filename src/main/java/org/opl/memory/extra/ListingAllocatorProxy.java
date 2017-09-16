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

    private final Map<Long, Long> allocatedBlockList;

    /**
     * Construct a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public ListingAllocatorProxy(Allocator delegate) {
        this.delegate = delegate;
        this.allocatedBlockList = new ConcurrentHashMap<>();
    }

    @Override
    public long allocate(long size) throws OutOfMemoryError {
        long address = delegate.allocate(size);

        allocatedBlockList.put(address, size);

        return address;
    }

    @Override
    public long reallocate(long address, long newSize) throws OutOfMemoryError {
        long newAddress = delegate.reallocate(address, newSize);

        allocatedBlockList.remove(address);
        allocatedBlockList.put(newAddress, newSize);

        return newAddress;
    }

    @Override
    public void free(long address) {
        delegate.free(address);
        allocatedBlockList.remove(address);
    }

    public Map<Long, Long> getAllocatedBlockList() {
        return new HashMap<>(allocatedBlockList);
    }

    public long getAllocatedBytes() {
        return allocatedBlockList.values().stream()
            .mapToLong(Long::longValue)
            .sum();
    }

    public long getAllocatedBlocks() {
        return allocatedBlockList.size();
    }

}
