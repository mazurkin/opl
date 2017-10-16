package org.opl.access.block;

/**
 * Additional superclass of {@link MemoryBlockDirect} to be used in development/test process. Performs
 * additional checks on all operations. Created with {@link MemoryBlockFactory}
 * @see MemoryBlockFactory
 */
public class MemoryBlockSafe implements MemoryBlock {

    private final MemoryBlock delegate;

    public MemoryBlockSafe(MemoryBlock delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate is null");
        }

        this.delegate = delegate;
    }

    @Override
    public MemoryBlock getParent() {
        return delegate.getParent();
    }

    @Override
    public boolean hasParent() {
        return delegate.hasParent();
    }

    @Override
    public MemoryBlockSafe slice(long offset, long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be positive or zero");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be positive or zero");
        }
        if (offset + size > this.address() + this.size()) {
            throw new IndexOutOfBoundsException("End of slice exceed current block");
        }

        return new MemoryBlockSafe(delegate.slice(offset, size));
    }

    @Override
    public MemoryBlockSafe sliceFirst(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be positive or zero");
        } else if (size > this.size()) {
            throw new IndexOutOfBoundsException("End of slice exceed current block");
        } else {
            return new MemoryBlockSafe(delegate.sliceFirst(size));
        }
    }

    @Override
    public MemoryBlockSafe sliceLast(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be positive or zero");
        } else if (size > this.size()) {
            throw new IndexOutOfBoundsException("End of slice exceed current block");
        } else {
            return new MemoryBlockSafe(delegate.sliceLast(size));
        }
    }

    @Override
    public void fill(byte value) {
        delegate.fill(value);
    }

    @Override
    public long size() {
        return delegate.size();
    }

    @Override
    public long address() {
        return delegate.address();
    }

    @Override
    public long address(long offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException("Offset can't be negative");
        } else if (offset > this.size()) {
            throw new IndexOutOfBoundsException("Offset exceeds the block size");
        } else {
            return delegate.address();
        }
    }

    @Override
    public long offset(long address) {
        long offset = address - delegate.address();
        if (offset < 0) {
            throw new IndexOutOfBoundsException("Offset can't be negative");
        } else if (offset > this.size()) {
            throw new IndexOutOfBoundsException("Offset exceeds the block size");
        } else {
            return offset;
        }
    }

    @Override
    public long available(long address) {
        long offset = address - delegate.address();
        if (offset < 0) {
            throw new IndexOutOfBoundsException("Offset can't be negative");
        } else if (offset > this.size()) {
            throw new IndexOutOfBoundsException("Offset exceeds the block size");
        } else {
            return size() - offset;
        }
    }

    @Override
    public void copyTo(MemoryBlockDirect that) {
        if (this.size() > this.size()) {
            throw new IndexOutOfBoundsException("This block is larger than that block");
        }

        delegate.copyTo(that);
    }

    @Override
    public void copyTo(MemoryBlockDirect that, long size) {
        if (size > this.size()) {
            throw new IndexOutOfBoundsException("Size is larger then this block");
        }
        if (size > that.size()) {
            throw new IndexOutOfBoundsException("Size is larger then that block");
        }

        delegate.copyTo(that, size);
    }

    @Override
    public void copyTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size) {
        if (thisOffset + size > this.size()) {
            throw new IndexOutOfBoundsException("Size with offset is larger then this block");
        }
        if (thatOffset + size > that.size()) {
            throw new IndexOutOfBoundsException("Size with offset is larger then that block");
        }

        delegate.copyTo(thisOffset, that, thatOffset, size);
    }

    @Override
    public int compareTo(MemoryBlockDirect that) {
        if (this.size() > this.size()) {
            throw new IndexOutOfBoundsException("This block is larger than that block");
        }

        return delegate.compareTo(that);
    }

    @Override
    public int compareTo(MemoryBlockDirect that, long size) {
        if (size > this.size()) {
            throw new IndexOutOfBoundsException("Size is larger then this block");
        }
        if (size > that.size()) {
            throw new IndexOutOfBoundsException("Size is larger then that block");
        }

        return delegate.compareTo(that, size);
    }

    @Override
    public int compareTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size) {
        if (thisOffset + size > this.size()) {
            throw new IndexOutOfBoundsException("Size with offset is larger then this block");
        }
        if (thatOffset + size > that.size()) {
            throw new IndexOutOfBoundsException("Size with offset is larger then that block");
        }

        return delegate.compareTo(thisOffset, that, thatOffset, size);
    }

}
