package org.opl.memory;

/**
 * Memory allocator abstraction. All implementations must be thread-safe.
 */
public interface Allocator {

    /**
     * Allocates specified amount of bytes. Byte values are undefined after allocation.
     * @param size Required memory size
     * @return Memory block address
     * @throws OutOfMemoryError Is thrown when allocator is our of memory
     * @throws AllocatorException Is thrown on violation of allocator's rule
     */
    long allocate(long size) throws AllocatorException, OutOfMemoryError;

    /**
     * Reallocates specified memory block with new size
     * @param address Memory block address
     * @param newSize New block size
     * @return Memory block address
     * @throws OutOfMemoryError exception may be thrown when allocator is out of memory
     * @throws AllocatorException Is thrown on violation of allocator's rule
     */
    long reallocate(long address, long newSize) throws AllocatorException, OutOfMemoryError;

    /**
     * Releases memory block
     * @param address Address of memory block
     * @throws AllocatorException Is thrown on violation of allocator's rule
     */
    void free(long address) throws AllocatorException;

}
