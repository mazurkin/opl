package org.opl.access;

import org.opl.access.block.MemoryBlock;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;
import org.opl.util.OplUtils;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class MemoryCharBuffer implements CharSequence {

    private final MemoryBlock block;

    private final int charCount;

    public MemoryCharBuffer(MemoryBlock block) {
        OplUtils.checkNotNull(block, "Block is not specified");

        long charCount = block.size() / Mem.CHAR_SIZE_BYTES;
        if (charCount > (long) Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Block is too huge for char buffer");
        }

        this.block = block;
        this.charCount = (int) charCount;
    }

    @Override
    public int length() {
        return this.charCount;
    }

    @Override
    public char charAt(int index) {
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("Index is out of bound");
        }

        long address = block.address(index >> 1);

        return Jvm.getChar(address);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (!isValidIndex(start)) {
            throw new IndexOutOfBoundsException("Start index is out of bound");
        }
        if (!isValidIndex(end)) {
            throw new IndexOutOfBoundsException("End index is out of bound");
        }

        if (start > end) {
            throw new IndexOutOfBoundsException("Start index is greater than end index");
        }

        MemoryBlock block = this.block.slice(start * Mem.CHAR_SIZE_BYTES, (end - start) * Mem.CHAR_SIZE_BYTES);

        return new MemoryCharBuffer(block);
    }

    private boolean isValidIndex(int index) {
        return 0 <= index && index < this.charCount;
    }

}
