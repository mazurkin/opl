package org.opl.memory;

import org.junit.Before;
import org.junit.Test;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Collection;

public class SystemAllocatorTest {

    private static final long[] SIZES = { 256, 512, 16 * Mem.KB, Mem.MB };

    private SystemAllocator allocator;

    @Before
    public void setUp() throws Exception {
        allocator = new SystemAllocator();
    }

    @Test
    public void simple() throws Exception {
        Collection<Long> addresses = new ArrayList<>(64);

        for (long size : SIZES) {
            long address = allocator.allocate(size);
            addresses.add(address);
        }

        for (long address : addresses) {
            allocator.free(address);
        }
    }

    @Test
    public void reallocation() throws Exception {
        long initialSize = 512;

        long address = allocator.allocate(initialSize);

        for (long size = initialSize; size < 16 * initialSize; size <<= 1) {
            address = allocator.reallocate(address, size);
        }

        allocator.free(address);
    }

}
