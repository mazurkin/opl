# OPL - Off-heap Primitives Library

OPL (Off-heap Primitives Library) implements a set of structures and algorithms 
which operate in off-heap memory.

* Structures can have size more than 2GB 
* all addressing by long not int
* Fast operation without bound check (safety bound check is also available as an option)

# JVM

`org.opl.platform.Jvm` wraps all calls to `sun.misc.Unsafe` make using these operations more comfortable 
without producing any compiler and import warning. 

    Jvm.putLong(address, value);

# Allocators

Allocator allows to allocate, reallocate and free memory blocks.

    Allocator allocator = new SystemAllocator();
    try {    
        long address = allocator.allocate(1024);
        try {        
            Jvm.putLong(address + 8, 0x1111_2222_3333_4444L);
            long v = Jvm.getLong(address + 8);
        } finally {
            allocator.free(address);
        }
    } finally { 
        allocator.close();
    }

# MemoryBlockFactory and MemoryBlock

`MemoryBlock` describes memory block as a pair (address, size) and has some address arithmetic methods.

`MemoryBlockFactory` is a wrapper around `Allocator` to allocate, reallocate and free memory block instances.

    
    MemoryBlockFactory factory = 
        new MemoryBlockFactory(new SystemAllocator(), true, false);
        
    try {
        MemoryBlock block = factory.allocate(1024);
        
        Jvm.putLong(block.address(8), 0x1111_2222_3333_4444L);
        long v = Jvm.getLong(block.adress(8);
    } finally {
        factory.close();
    }    
    
# Structures

## OplBitSet

Off-heap bit set

        
    OplBitSet bitset = new OplBitSetImpl(memBlkFactory, 3 * Mem.GB);
    try {
        bitset.set(10000);
        bitset.toggle(10001);
    } finally {
        bitset.close(); // free memory
    }       


## OplBloomFilter

Off-heap [Bloom filter](https://en.wikipedia.org/wiki/Bloom_filter)

    OplBloomFilter filter = new OplBloomFilterImpl(memBlkFactory,
    try {
        filter.put(0x0123);
        Assert.assertTrue(filter.contains(0x0123)); // always true
        Assert.assertFalse(filter.contains(0x0567)); // actually may fail because 
    } finally {
        filter.close(); // free memory
    }
     
# Safety

Programming with off-heap is not safe as you can crash the JVM instead 
of getting well-structured exception.

So on development/test phase use safety options.

## Safe MemoryBlockFactory

MemoryBlockFactory can allocate safe block which check indexing on operations.

    # Create a factory that produces "checking" blocks
    boolean safe = true;
    
    MemoryBlockFactory factory = 
        new MemoryBlockFactory(new SystemAllocator(), true, safe);

    MemoryBlock block = factory.allocate(1024);
    
    // IndexOfBoundException - not SIGSERV  
    block.address(1025); 
    
## RegistryAllocatorProxy

RegistryAllocatorProxy is a wrapper real allocator that helps to track all allocated block addresses

    Allocator allocator = new RegistryAllocatorProxy(
        new SystemAllocator(), true);
        
    // We won't free this address
    long address = allocate.allocate(1024);
        
    // Release some invalid address - AllocatorException is raised
    allocator.free(0x1234);      
    
    // will get IllegalStateException as one block has not been released
    allocator.close();
     
## DefensiveAllocatorProxy

DefensiveAllocatorProxy allocates safety areas around the real block so it can detect dangerous 
operation made outside a block.

    Allocator allocator = new DefensiveAllocatorProxy(  
        new SystemAllocator(), true);
        
    // We will make an illegal write
    long address = allocate.allocate(1024);
    
    // Write outside the allocated block
    Jvm.putInt(address + 1024, 0x1234);
    
    // will get AllocatorException as the block is corrupted
    allocate.free(address);
    
You can combine both proxies:

     Allocator allocator = new DefensiveAllocatorProxy( 
        new RegistryAllocatorProxy(new SystemAllocator(), true), 
        true);      