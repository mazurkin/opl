package org.opl.memory.extra;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opl.memory.SystemAllocator;
import org.opl.platform.Mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

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

        long allocated = allocator.getListing().entrySet().stream()
                .mapToLong(Map.Entry::getValue)
                .sum();

        Assert.assertEquals(Arrays.stream(SIZES).sum(), allocated);
        Assert.assertEquals(SIZES.length, allocator.getListing().size());

        for (long address : addresses) {
            allocator.free(address);
        }

        Assert.assertEquals(0, allocator.getListing().size());
    }



}