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

    private final Map<Long, Long> listing;

    /**
     * Construct a listing proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public ListingAllocatorProxy(Allocator delegate) {
        this.delegate = delegate;
        this.listing = new ConcurrentHashMap<>();
    }

    @Override
    public long allocate(long size) throws OutOfMemoryError {
        long address = delegate.allocate(size);

        listing.put(address, size);

        return address;
    }

    @Override
    public long reallocate(long address, long newSize) throws OutOfMemoryError {
        long newAddress = delegate.reallocate(address, newSize);

        listing.remove(address);
        listing.put(newAddress, newSize);

        return newAddress;
    }

    @Override
    public void free(long address) {
        delegate.free(address);
        listing.remove(address);
    }

    public Map<Long, Long> getListing() {
        return new HashMap<>(listing);
    }
}
