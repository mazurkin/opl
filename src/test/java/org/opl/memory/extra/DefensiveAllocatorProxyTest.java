package org.opl.memory.extra;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opl.memory.SystemAllocator;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DefensiveAllocatorProxyTest {

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

        Assert.assertEquals(Arrays.stream(SIZES).sum(), allocator.getAllocatedBytes());
        Assert.assertEquals(SIZES.length, allocator.getAllocatedBlocks());

        for (long address : addresses) {
            allocator.free(address);
        }

        Assert.assertEquals(0, allocator.getAllocatedBytes());
        Assert.assertEquals(0, allocator.getAllocatedBlocks());
    }

    @Test
    public void doubleFreeCall() throws Exception {
        long address = allocator.allocate(1024);

        allocator.free(address);

        try {
            allocator.free(address);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void corruptionOnHeader() throws Exception {
        long address = allocator.allocate(1024);

        Jvm.putByte(address - 2, (byte) 0xFF);
        Jvm.putByte(address - 1, (byte) 0xFE);

        try {
            allocator.free(address);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void corruptionOnFooter() throws Exception {
        long address = allocator.allocate(1024);

        Jvm.putByte(address + 1024, (byte) 0xFF);
        Jvm.putByte(address + 1025, (byte) 0xFE);

        try {
            allocator.free(address);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}