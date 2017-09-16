package org.opl.memory;

import org.opl.platform.Jvm;

/**
 * System allocator. Delegates all requests to sun.misc.Unsafe instance.
 */
public class SystemAllocator implements Allocator {

    @Override
    public long allocate(long size) {
        return Jvm.allocateMemory(size);
    }

    @Override
    public long reallocate(long address, long newSize) {
        return Jvm.reallocateMemory(address, newSize);
    }

    @Override
    public void free(long address) {
        Jvm.freeMemory(address);
    }

}
