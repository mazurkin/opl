package org.opl.allocator;

import org.opl.platform.Jvm;
import org.opl.util.OplUtils;

/**
 * System allocator. Delegates all requests to sun.misc.Unsafe instance.
 */
public class SystemAllocator implements Allocator {

    @Override
    public long allocate(long size) {
        OplUtils.checkGreaterThanZero(size, "Size must be greater than 0");

        return Jvm.allocateMemory(size);
    }

    @Override
    public long reallocate(long address, long newSize) {
        OplUtils.checkGreaterThanZero(newSize, "Size must be greater than 0");

        return Jvm.reallocateMemory(address, newSize);
    }

    @Override
    public void free(long address) {
        Jvm.freeMemory(address);
    }

    @Override
    public void close() throws Exception {
        // do nothing with system allocator
    }
}
