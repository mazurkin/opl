package org.opl.memory.trace;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opl.memory.AllocatorException;
import org.opl.memory.SystemAllocator;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DefensiveAllocatorProxyTest {

    private static final long[] SIZES = { 256, 512, 16 * Mem.KB, Mem.MB };

    private DefensiveAllocatorProxy allocator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        allocator = new DefensiveAllocatorProxy(new SystemAllocator());
    }

    @Test
    public void simple() throws Exception {
        Collection<Long> addresses = new ArrayList<>(64);

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
    public void reallocate() throws Exception {
        long a1 = allocator.allocate(1024);
        Assert.assertEquals(1, allocator.getAllocatedBlocks());
        Assert.assertEquals(1024, allocator.getAllocatedBytes());

        long a2 = allocator.reallocate(a1, 2048);
        Assert.assertEquals(1, allocator.getAllocatedBlocks());
        Assert.assertEquals(2048, allocator.getAllocatedBytes());

        allocator.free(a2);
        Assert.assertEquals(0, allocator.getAllocatedBlocks());
        Assert.assertEquals(0, allocator.getAllocatedBytes());
    }

    @Test
    public void doubleFreeCall() throws Exception {
        long address = allocator.allocate(1024);

        allocator.free(address);

        expectedException.expect(AllocatorException.class);
        expectedException.expectMessage(Long.toHexString(address));
        allocator.free(address);
    }

    @Test
    public void corruptionOnHeader() throws Exception {
        long address = allocator.allocate(1024);

        Jvm.putByte(address - 2, (byte) 0xFF);
        Jvm.putByte(address - 1, (byte) 0xFE);

        expectedException.expect(AllocatorException.class);
        expectedException.expectMessage(Long.toHexString(address));
        allocator.free(address);
    }

    @Test
    public void corruptionOnFooter() throws Exception {
        long address = allocator.allocate(1024);

        Jvm.putByte(address + 1024, (byte) 0xFF);
        Jvm.putByte(address + 1025, (byte) 0xFE);

        expectedException.expect(AllocatorException.class);
        expectedException.expectMessage(Long.toHexString(address));
        allocator.free(address);
    }
}
