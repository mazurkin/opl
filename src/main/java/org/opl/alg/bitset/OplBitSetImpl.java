package org.opl.alg.bitset;

import org.opl.access.block.MemoryBlock;
import org.opl.access.block.MemoryBlockFactory;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class OplBitSetImpl implements OplBitSet {

    private final MemoryBlockFactory factory;

    private final MemoryBlock block;

    private final long size;

    public OplBitSetImpl(MemoryBlockFactory factory, long size) {
        this.factory = factory;
        this.size = size;

        if (size > Long.MAX_VALUE - Mem.BYTE_SIZE_BITS) {
            throw new IllegalArgumentException("Size is too huge");
        }

        long byteSize = (size + Mem.BYTE_SIZE_BITS - 1) / Mem.BYTE_SIZE_BITS;
        this.block = factory.allocate(byteSize);
        this.block.fill((byte) 0);
    }

    @Override
    public void close() throws Exception {
        factory.free(block);
    }

    @Override
    public void set(long bit) {
        checkBitIndex(bit);

        long offset = Mem.offsetFromBitIndex(bit);
        byte mask = Mem.maskFromBitIndex(bit);
        long address = block.address(offset);

        byte b = Jvm.getByte(address);
        b = Mem.setByteBits(b, mask);
        Jvm.putByte(address, b);
    }

    @Override
    public void clean(long bit) {
        checkBitIndex(bit);

        long offset = Mem.offsetFromBitIndex(bit);
        byte mask = Mem.maskFromBitIndex(bit);
        long address = block.address(offset);

        byte b = Jvm.getByte(address);
        b = Mem.clearByteBits(b, mask);
        Jvm.putByte(address, b);
    }

    @Override
    public void toggle(long bit) {
        checkBitIndex(bit);

        long offset = Mem.offsetFromBitIndex(bit);
        byte mask = Mem.maskFromBitIndex(bit);
        long address = block.address(offset);

        byte b = Jvm.getByte(address);
        b = Mem.toggleByteBits(b, mask);
        Jvm.putByte(address, b);
    }

    @Override
    public boolean get(long bit) {
        checkBitIndex(bit);

        long offset = Mem.offsetFromBitIndex(bit);
        byte mask = Mem.maskFromBitIndex(bit);
        long address = block.address(offset);

        byte b = Jvm.getByte(address);
        return Mem.checkByteBitsSet(b, mask);
    }

    @Override
    public void reset() {
        block.fill((byte) 0);
    }

    @Override
    public long size() {
        return size;
    }

    private void checkBitIndex(long index) {
        boolean valid = (0 <= index) && (index < size);

        if (!valid) {
            throw new IndexOutOfBoundsException("Bit index is invalid");
        }
    }
}
