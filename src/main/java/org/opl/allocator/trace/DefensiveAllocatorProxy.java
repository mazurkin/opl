package org.opl.allocator.trace;

import org.opl.allocator.Allocator;
import org.opl.allocator.AllocatorException;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;
import org.opl.util.OplUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Defensive proxy around a real allocator. Useful for debugging. Adds extra bytes to each allocated block.</p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Keeps a balance counter for allocation and de-allocation calls. So if a balance is greater
 *     than 0 then it means you have a memory leak.</li>
 *     <li>Allocates additional header and footer area around real memory block. It allows to detect
 *     out-of-range writes.</li>
 *     <li>Detects repeatable free calls on the same address</li>
 * </ul>
 *
 * <p>The structure of allocated block</p>
 * <pre>
 *  <------- header -------->                   <- footer ->
 * +------------+------------+----- ..... -----+------------+
 * | block size |   header   |   memory block  |   footer   |
 * |            |   stamp    |  for the caller |   stamp    |
 * | (8 bytes)  | (8 bytes)  |  (`size` bytes) | (8 bytes)  |
 * +------------+------------+----- ..... -----+------------+
 * </pre>
 *
 * <p>Sample usage in a test:</p>
 * <pre>
 * private DefensiveAllocatorProxy allocator;
 *
 *{@literal @}Before
 * public void setUp() throws Exception {
 *     this.allocator = new DefensiveAllocatorProxy(new SystemAllocator());
 * }
 *
 *{@literal @}After
 * public void tearDown() throws Exception {
 *     Assert.assertEquals(0, this.allocator.getAllocatedBlocks());
 * }
 * </pre>
 *
 * <p>Can be combined with {@link RegistryAllocatorProxy}</p>
 *
 * <p>Will not close delegate allocator - so you have to close the delegate allocator explicitly.</p>
 */
public class DefensiveAllocatorProxy implements Allocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefensiveAllocatorProxy.class);

    private static final long HEADER_SIZE_BYTES = Mem.LONG_SIZE_BYTES + Mem.LONG_SIZE_BYTES;

    private static final long FOOTER_SIZE_BYTES = Mem.LONG_SIZE_BYTES;

    private static final long HEADER_PROTECTOR_STAMP = 0xEF01_2345_6789_ABCDL;

    private static final long FOOTER_PROTECTOR_STAMP = 0xEDCB_A987_6543_210FL;

    private static final long CLEANUP_VALUE = 0xFFFF_FFFF_FFFF_FFFFL;

    private final Allocator delegate;

    private final boolean delegateIsOwned;

    private final AtomicLong allocatedBytes;

    private final AtomicLong allocatedBlocks;

    /**
     * Construct a defensive proxy around delegate allocator
     * @param delegate Delegate allocator instance (will not be automatically closed)
     * @param delegateIsOwned If set to <code>true</code> then delegate will also be closed on closing
     */
    public DefensiveAllocatorProxy(@Nonnull Allocator delegate,
                                   boolean delegateIsOwned)
    {
        OplUtils.checkNotNull(delegate, "Delegate is not set");

        this.delegate = delegate;
        this.delegateIsOwned = delegateIsOwned;
        this.allocatedBytes = new AtomicLong(0);
        this.allocatedBlocks = new AtomicLong(0);
    }

    public void reset() {
        this.allocatedBytes.set(0);
        this.allocatedBlocks.set(0);
    }

    @Override
    public void close() throws Exception {
        if (delegateIsOwned) {
            delegate.close();
        }

        long blocks = allocatedBlocks.get();
        if (blocks != 0) {
            throw new IllegalStateException("There are " + blocks
                + " unreleased memory blocks. You have a memory leak.");
        }
    }

    @Override
    public long allocate(long externalSize) {
        OplUtils.checkGreaterThanZero(externalSize, "Size must be greater than 0");

        final long delegateSize = externalSize + HEADER_SIZE_BYTES + FOOTER_SIZE_BYTES;
        final long delegateAddress = delegate.allocate(delegateSize);
        final long externalAddress = delegateAddress + HEADER_SIZE_BYTES;

        allocatedBytes.addAndGet(externalSize);
        allocatedBlocks.incrementAndGet();

        storeStamp(delegateAddress, externalSize);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("%d bytes are allocated at [0x%016x]", externalSize, externalAddress));
        }

        return externalAddress;
    }

    @Override
    public long reallocate(long externalAddress, long newExternalSize) {
        OplUtils.checkGreaterThanZero(newExternalSize, "Size must be greater than 0");

        final long delegateAddress = externalAddress - HEADER_SIZE_BYTES;
        final long externalSize = fetchStamp(delegateAddress);

        clearStamp(delegateAddress, externalSize);

        final long newDelegateSize = newExternalSize + HEADER_SIZE_BYTES + FOOTER_SIZE_BYTES;
        final long newDelegateAddress = delegate.reallocate(delegateAddress, newDelegateSize);
        final long newExternalAddress = newDelegateAddress + HEADER_SIZE_BYTES;

        allocatedBytes.addAndGet(newExternalSize - externalSize);

        storeStamp(newDelegateAddress, newExternalSize);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("%d bytes at [0x%016x] are reallocated to %d bytes at [0x%016x]",
                    externalSize, externalAddress, newExternalSize, newExternalAddress));
        }

        return newExternalAddress;
    }

    @Override
    public void free(long externalAddress) {
        final long delegateAddress = externalAddress - HEADER_SIZE_BYTES;
        final long externalSize = fetchStamp(delegateAddress);

        clearStamp(delegateAddress, externalSize);

        delegate.free(delegateAddress);

        allocatedBytes.addAndGet(-externalSize);
        allocatedBlocks.decrementAndGet();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("%d bytes at [0x%016x] are freed", externalSize, externalAddress));
        }
    }

    public long getAllocatedBytes() {
        return allocatedBytes.get();
    }

    public long getAllocatedBlocks() {
        return allocatedBlocks.get();
    }

    private static void storeStamp(long delegateAddress, long externalSize) {
        Jvm.putLong(delegateAddress, externalSize);
        Jvm.putLong(delegateAddress + Mem.LONG_SIZE_BYTES, HEADER_PROTECTOR_STAMP);
        Jvm.putLong(delegateAddress + HEADER_SIZE_BYTES + externalSize, FOOTER_PROTECTOR_STAMP);
    }

    private static void clearStamp(long delegateAddress, long externalSize) {
        Jvm.putLong(delegateAddress, CLEANUP_VALUE);
        Jvm.putLong(delegateAddress + Mem.LONG_SIZE_BYTES, CLEANUP_VALUE);
        Jvm.putLong(delegateAddress + HEADER_SIZE_BYTES + externalSize, CLEANUP_VALUE);
    }

    private static long fetchStamp(long delegateAddress) {
        final long externalAddress = delegateAddress + HEADER_SIZE_BYTES;

        final long externalSize = Jvm.getLong(delegateAddress);
        if (externalSize <= 0) {
            throw new AllocatorException(
                String.format("Block size is invalid on [0x%016x]. Block has been freed or corrupted",
                    externalAddress));
        }

        final long actualHeaderStamp = Jvm.getLong(delegateAddress + Mem.LONG_SIZE_BYTES);
        if (actualHeaderStamp != HEADER_PROTECTOR_STAMP) {
            throw new AllocatorException(
                String.format("Header protection error: 0x%016x (must be 0x%016x) for [0x%016x]",
                    actualHeaderStamp, HEADER_PROTECTOR_STAMP, externalAddress));
        }

        final long actualFooterStamp = Jvm.getLong(delegateAddress + HEADER_SIZE_BYTES + externalSize);
        if (actualFooterStamp != FOOTER_PROTECTOR_STAMP) {
            throw new AllocatorException(
                String.format("Footer protection error: 0x%016x (must be 0x%016x) for [0x%016x]",
                    actualFooterStamp, FOOTER_PROTECTOR_STAMP, externalAddress));
        }

        return externalSize;
    }
}
