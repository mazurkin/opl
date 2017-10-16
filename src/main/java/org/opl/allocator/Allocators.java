package org.opl.allocator;

import org.opl.access.block.MemoryBlockFactory;
import org.opl.allocator.trace.DefensiveAllocatorProxy;
import org.opl.allocator.trace.RegistryAllocatorProxy;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

public final class Allocators {

    private Allocators() {
        // nothing to do in an utility class
    }

    public static Allocator createSystemAllocator() {
        return new SystemAllocator();
    }

    public static Allocator createTestAllocator() {
        Allocator systemAllocator = createSystemAllocator();
        Allocator testAllocator = new DefensiveAllocatorProxy(systemAllocator, true);
        return new RegistryAllocatorProxy(testAllocator, true);
    }

    public static MemoryBlockFactory createMemoryBlockFactory() {
        return new MemoryBlockFactory(createSystemAllocator(), true, false);
    }

    public static MemoryBlockFactory createMemoryBlockFactory(@Nonnull @WillNotClose Allocator allocator) {
        return new MemoryBlockFactory(allocator, false, false);
    }

    public static MemoryBlockFactory createTestMemoryBlockFactory() {
        return new MemoryBlockFactory(createTestAllocator(), true, true);
    }

    public static MemoryBlockFactory createTestMemoryBlockFactory(@Nonnull @WillNotClose Allocator allocator) {
        return new MemoryBlockFactory(allocator, false, true);
    }
}
