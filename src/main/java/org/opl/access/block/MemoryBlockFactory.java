package org.opl.access.block;

import org.opl.allocator.Allocator;
import org.opl.util.OplUtils;

import javax.annotation.Nonnull;

/**
 * Abstraction to switch between safe and unsafe memory block implementations
 */
public class MemoryBlockFactory implements AutoCloseable {

    private final Allocator allocator;

    private final boolean allocatorIsOwned;

    private final boolean checking;

    public MemoryBlockFactory(@Nonnull Allocator allocator,
                              boolean allocatorIsOwned,
                              boolean checking)
    {
        OplUtils.checkNotNull(allocator, "Allocator is not specified");

        this.allocator = allocator;
        this.allocatorIsOwned = allocatorIsOwned;
        this.checking = checking;
    }

    @Override
    public void close() throws Exception {
        if (allocatorIsOwned) {
            allocator.close();
        }
    }

    private MemoryBlock createBlock(long address, long size) {
        MemoryBlockDirect direct = new MemoryBlockDirect(address, size);

        if (checking) {
            return new MemoryBlockSafe(direct);
        } else {
            return direct;
        }
    }

    /**
     * Allocated the memory block of the specified size
     * @param size Size of the memory block to be allocated
     * @return The memory block
     */
    public MemoryBlock allocate(long size) {
        long address = allocator.allocate(size);
        return createBlock(address, size);
    }

    /**
     * Reallocates the specified memory block
     * @param block Memory block to reallocate (gets invalid after reallocation)
     * @param newSize New size of the block
     * @return New memory block with new size
     */
    public MemoryBlock reallocate(@Nonnull MemoryBlock block, long newSize) {
        if (block.hasParent()) {
            throw new IllegalStateException("Can't perform operation on child block of " + block.getParent());
        }

        long newAddress = allocator.reallocate(block.address(), newSize);
        return createBlock(newAddress, newSize);
    }

    /**
     * Releases the specified memory block
     * @param block Memory block to release
     * @see Allocator#free(long)
     */
    public void free(@Nonnull MemoryBlock block) {
        if (block.hasParent()) {
            throw new IllegalStateException("Can't perform operation on child block of " + block.getParent());
        }

        allocator.free(block.address());
    }

}
