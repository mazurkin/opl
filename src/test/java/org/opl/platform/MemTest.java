package org.opl.platform;

import org.junit.Assert;
import org.junit.Test;

public class MemTest {

    @Test
    public void bitMax() throws Exception {
        Assert.assertEquals(Long.MIN_VALUE, Mem.bit(Long.SIZE - 1));
        Assert.assertEquals(Long.MIN_VALUE, Mem.longBit(Long.SIZE - 1));
        Assert.assertEquals(Integer.MIN_VALUE, Mem.intBit(Integer.SIZE - 1));
        Assert.assertEquals(Short.MIN_VALUE, Mem.shortBit(Short.SIZE - 1));
        Assert.assertEquals(Byte.MIN_VALUE, Mem.byteBit(Byte.SIZE - 1));
    }

    @Test
    public void maskByte() throws Exception {
        Assert.assertEquals((byte) 0b0001_1000, Mem.byteMask(3, 5));
        Assert.assertEquals((byte) 0b0000_0000, Mem.byteMask(1, 1));
        Assert.assertEquals((byte) 0b0100_0000, Mem.byteMask(6, 7));
        Assert.assertEquals((byte) 0b1000_0000, Mem.byteMask(7, 8));
    }

    @Test
    public void maskAll() throws Exception {
        Assert.assertEquals(0b0100_0000, Mem.byteMask(6, 7));
        Assert.assertEquals(0b0100_0000, Mem.shortMask(6, 7));
        Assert.assertEquals(0b0100_0000, Mem.intMask(6, 7));
        Assert.assertEquals(0b0100_0000, Mem.longMask(6, 7));

        Assert.assertEquals(0x8000_0000_0000_0000L, Mem.longMask(63, 64));
        Assert.assertEquals(0x0000_0000_0000_0001L, Mem.longMask(0, 1));
        Assert.assertEquals(0x0000_0000_0000_0000L, Mem.longMask(0, 0));
        Assert.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, Mem.longMask(0, 64));
    }

}
