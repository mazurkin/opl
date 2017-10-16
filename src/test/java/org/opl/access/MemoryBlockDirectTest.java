package org.opl.access;

import org.junit.Assert;
import org.junit.Test;
import org.opl.access.block.MemoryBlockDirect;

public class MemoryBlockDirectTest {

    @Test
    public void testEmpty() throws Exception {
        MemoryBlockDirect b = new MemoryBlockDirect(0, 0);
        Assert.assertEquals(0, b.address());
        Assert.assertEquals(0, b.size());
        Assert.assertEquals(MemoryBlockDirect.EMPTY, b);
    }

    @Test
    public void testSized() throws Exception {
        MemoryBlockDirect b = new MemoryBlockDirect(0xFFFF_EEEEL, 123);
        Assert.assertEquals(0xFFFF_EEEEL, b.address());
        Assert.assertEquals(123, b.size());
    }

    @Test
    public void testSliceFirst() throws Exception {
        MemoryBlockDirect b = new MemoryBlockDirect(10_200_300L, 10000);

        Assert.assertEquals(new MemoryBlockDirect(10_200_300L, 100), b.sliceFirst(100));
    }

    @Test
    public void testSliceLast() throws Exception {
        MemoryBlockDirect b = new MemoryBlockDirect(10_200_300L, 10000);

        Assert.assertEquals(new MemoryBlockDirect(10_210_200L, 100), b.sliceLast(100));
    }

    @Test
    public void testSlice() throws Exception {
        MemoryBlockDirect b = new MemoryBlockDirect(10_200_300L, 10000);

        Assert.assertEquals(new MemoryBlockDirect(10_200_400L, 100), b.slice(100, 100));
    }
}
