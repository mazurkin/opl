package org.opl.memory;

import org.opl.platform.Jvm;

/**
 * System allocator. Delegates all requests to sun.misc.Unsafe instance.
 */
public class SystemAllocator implements Allocator {

    @Override
    public long allocate(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        return Jvm.allocateMemory(size);
    }

    @Override
    public long reallocate(long address, long newSize) {
        if (newSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        return Jvm.reallocateMemory(address, newSize);
    }

    @Override
    public void free(long address) {
        Jvm.freeMemory(address);
    }

}
