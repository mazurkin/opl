package org.opl.memory.extra;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opl.memory.SystemAllocator;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ListingAllocatorProxyTest {

    private static final long[] SIZES = { 256, 512, 16 * Mem.KB, Mem.GB };

    private ListingAllocatorProxy allocator;

    @Before
    public void setUp() throws Exception {
        allocator = new ListingAllocatorProxy(new SystemAllocator());
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
        Assert.assertEquals(SIZES.length, allocator.getAllocatedBlockList().size());

        for (long address : addresses) {
            allocator.free(address);
        }

        Assert.assertEquals(0, allocator.getAllocatedBytes());
        Assert.assertEquals(0, allocator.getAllocatedBlocks());
        Assert.assertEquals(0, allocator.getAllocatedBlockList().size());
    }

    @Test
    public void reallocate() throws Exception {
        long a1 = allocator.allocate(1024);
        Assert.assertEquals(1, allocator.getAllocatedBlocks());
        Assert.assertEquals(1024, allocator.getAllocatedBytes());
        Assert.assertEquals(1, allocator.getAllocatedBlockList().size());

        long a2 = allocator.reallocate(a1, 2048);
        Assert.assertEquals(1, allocator.getAllocatedBlocks());
        Assert.assertEquals(2048, allocator.getAllocatedBytes());
        Assert.assertEquals(1, allocator.getAllocatedBlockList().size());

        allocator.free(a2);
        Assert.assertEquals(0, allocator.getAllocatedBlocks());
        Assert.assertEquals(0, allocator.getAllocatedBytes());
        Assert.assertEquals(0, allocator.getAllocatedBlockList().size());
    }


}
