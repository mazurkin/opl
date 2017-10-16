package org.opl.access.block;

import org.opl.platform.Jvm;
import org.opl.util.OplUtils;

import java.util.Objects;

public class MemoryBlockDirect implements MemoryBlock {

    public static final MemoryBlockDirect EMPTY = new MemoryBlockDirect(0, 0);

    private final long address;

    private final long size;

    private final MemoryBlock parent;

    public MemoryBlockDirect(long address, long size) {
        this(address, size, null);
    }

    public MemoryBlockDirect(long address, long size, MemoryBlock parent) {
        OplUtils.checkGreaterOrEqualZero(size, "Size must be positive or zero");

        this.address = address;
        this.size = size;
        this.parent = parent;
    }

    @Override
    public MemoryBlock getParent() {
        return this.parent;
    }

    @Override
    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public long address() {
        return address;
    }

    @Override
    public long address(long offset) {
        return address + offset;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public long offset(long address) {
        return address - this.address;
    }

    @Override
    public long available(long address) {
        return size - (address - this.address);
    }

    @Override
    public MemoryBlock slice(long offset, long size) {
        MemoryBlock parent = this.parent != null ? this.parent : this;

        return new MemoryBlockDirect(this.address + offset, size, parent);
    }

    @Override
    public MemoryBlock sliceFirst(long size) {
        MemoryBlock parent = this.parent != null ? this.parent : this;

        return new MemoryBlockDirect(this.address, size, parent);
    }

    @Override
    public MemoryBlock sliceLast(long size) {
        MemoryBlock parent = this.parent != null ? this.parent : this;

        return new MemoryBlockDirect(this.address + this.size - size, size, parent);
    }

    @Override
    public void fill(byte value) {
        Jvm.setMemory(this.address, this.size, value);
    }

    @Override
    public void copyTo(MemoryBlockDirect that) {
        copyTo(0, that, 0, this.size);
    }

    @Override
    public void copyTo(MemoryBlockDirect that, long size) {
        copyTo(0, that, 0, size);
    }

    @Override
    public void copyTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size) {
        long a1 = this.address(thisOffset);
        long a2 = that.address(thatOffset);
        Jvm.copyMemory(a1, a2, size);
    }

    @Override
    public int compareTo(MemoryBlockDirect that) {
        return compareTo(0, that, 0, this.size);
    }

    @Override
    public int compareTo(MemoryBlockDirect that, long size) {
        return compareTo(0, that, 0, size);
    }

    @Override
    public int compareTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size) {
        long a1 = this.address(thisOffset);
        long a2 = that.address(thatOffset);

        for (long i = 0; i < size; i++, a1++, a2++) {
            int b1 = Byte.toUnsignedInt(Jvm.getByte(a1));
            int b2 = Byte.toUnsignedInt(Jvm.getByte(a2));

            int r = Integer.compare(b1, b2);
            if (r != 0) {
                return r;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemoryBlockDirect that = (MemoryBlockDirect) o;

        return this.address == that.address
            && this.size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address, this.size);
    }

    @Override
    public String toString() {
        return String.format("[0x%016x]/%d", this.address, this.size);
    }
}
