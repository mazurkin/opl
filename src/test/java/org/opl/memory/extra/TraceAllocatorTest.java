package org.opl.memory.extra;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opl.memory.SystemAllocator;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Collection;

public class TraceAllocatorTest {

    private static final long[] SIZES = { 256, 512, 16 * Mem.KB, Mem.GB };

    private DefensiveAllocatorProxy allocator;

    @Before
    public void setUp() throws Exception {
        allocator = new DefensiveAllocatorProxy(new SystemAllocator());
    }

    @Test
    public void simple() throws Exception {
        Collection<Long> addresses = new ArrayList<Long>(64);

        for (long size : SIZES) {
            long address = allocator.allocate(size);
            addresses.add(address);
        }

        for (long address : addresses) {
            allocator.free(address);
        }

        Assert.assertEquals(0, allocator.getBalance());
    }

}