package org.opl.access.block;

/**
 * <p>Immutable wrapper for memory block described by pair (address, size)</p>
 * <p>Includes some address arithmetic and safety wrapper</p>
 */
public interface MemoryBlock {

    MemoryBlock getParent();

    boolean hasParent();

    long address();

    long address(long offset);

    long size();

    long offset(long address);

    long available(long address);

    void fill(byte value);

    void copyTo(MemoryBlockDirect that);

    void copyTo(MemoryBlockDirect that, long size);

    void copyTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size);

    int compareTo(MemoryBlockDirect that);

    int compareTo(MemoryBlockDirect that, long size);

    int compareTo(long thisOffset, MemoryBlockDirect that, long thatOffset, long size);

    MemoryBlock slice(long offset, long size);

    MemoryBlock sliceFirst(long size);

    MemoryBlock sliceLast(long size);

}
