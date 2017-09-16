package org.opl.memory.trace;

import org.opl.memory.Allocator;
import org.opl.memory.AllocatorException;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Defensive proxy around a real allocator. Useful for debugging. Adds extra bytes to any allocated block.</p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Keeps a balance counter for allocation and de-allocation calls. So if a balance is greater
 *     than 0 then it means you have a memory leak.</li>
 *     <li>Allocates additional header and footer area around real memory block. It allows to detect
 *     out-of-range writes.</li>
 *     <li>Detects repeatable free calls on the same address</li>
 * </ul>
 */
public class DefensiveAllocatorProxy implements Allocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefensiveAllocatorProxy.class);

    private static final long HEADER_SIZE = Mem.LONG_SIZE + Mem.LONG_SIZE;

    private static final long FOOTER_SIZE = Mem.LONG_SIZE;

    private static final long HEADER_PROTECTOR_STAMP = 0xEF01_2345_6789_ABCDL;

    private static final long FOOTER_PROTECTOR_STAMP = 0xEDCB_A987_6543_210FL;

    private static final long CLEANUP_VALUE = 0xFFFF_FFFF_FFFF_FFFFL;

    private final Allocator delegate;

    private final AtomicLong allocatedBytes;

    private final AtomicLong allocatedBlocks;

    /**
     * Construct a defensive proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public DefensiveAllocatorProxy(Allocator delegate) {
        this.delegate = delegate;
        this.allocatedBytes = new AtomicLong(0);
        this.allocatedBlocks = new AtomicLong(0);
    }

    @Override
    public long allocate(long externalSize) {
        if (externalSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        final long delegateSize = externalSize + HEADER_SIZE + FOOTER_SIZE;
        final long delegateAddress = delegate.allocate(delegateSize);
        final long externalAddress = delegateAddress + HEADER_SIZE;

        allocatedBytes.addAndGet(externalSize);
        allocatedBlocks.incrementAndGet();

        storeStamp(delegateAddress, externalSize);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes are allocated at {}", externalSize, Long.toHexString(externalAddress));
        }

        return externalAddress;
    }

    @Override
    public long reallocate(long externalAddress, long newExternalSize) {
        if (newExternalSize <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        final long delegateAddress = externalAddress - HEADER_SIZE;
        final long externalSize = fetchStamp(delegateAddress);

        clearStamp(delegateAddress, externalSize);

        final long newDelegateSize = newExternalSize + HEADER_SIZE + FOOTER_SIZE;
        final long newDelegateAddress = delegate.reallocate(delegateAddress, newDelegateSize);
        final long newExternalAddress = newDelegateAddress + HEADER_SIZE;

        allocatedBytes.addAndGet(newExternalSize - externalSize);

        storeStamp(newDelegateAddress, newExternalSize);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes at {} are reallocated to {} bytes at {}",
                    externalSize, Long.toHexString(externalAddress),
                    newExternalSize, Long.toHexString(newExternalAddress));
        }

        return newExternalAddress;
    }

    @Override
    public void free(long externalAddress) {
        final long delegateAddress = externalAddress - HEADER_SIZE;
        final long externalSize = fetchStamp(delegateAddress);

        clearStamp(delegateAddress, externalSize);

        delegate.free(delegateAddress);

        allocatedBytes.addAndGet(-externalSize);
        allocatedBlocks.decrementAndGet();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes at {} are freed", externalSize, Long.toHexString(externalAddress));
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
        Jvm.putLong(delegateAddress + Mem.LONG_SIZE, HEADER_PROTECTOR_STAMP);
        Jvm.putLong(delegateAddress + HEADER_SIZE + externalSize, FOOTER_PROTECTOR_STAMP);
    }

    private static void clearStamp(long delegateAddress, long externalSize) {
        Jvm.putLong(delegateAddress, CLEANUP_VALUE);
        Jvm.putLong(delegateAddress + Mem.LONG_SIZE, CLEANUP_VALUE);
        Jvm.putLong(delegateAddress + HEADER_SIZE + externalSize, CLEANUP_VALUE);
    }

    private static long fetchStamp(long delegateAddress) {
        final long externalSize = Jvm.getLong(delegateAddress);
        if (externalSize <= 0) {
            throw new AllocatorException(
                    String.format("Block %s size is not set. Block has been freed or corrupted",
                        Long.toHexString(delegateAddress)));
        }

        final long actualHeaderStamp = Jvm.getLong(delegateAddress + Mem.LONG_SIZE);
        if (actualHeaderStamp != HEADER_PROTECTOR_STAMP) {
            throw new AllocatorException(String.format("Header protection error: %s (must be %s) for addr %s",
                    Long.toHexString(actualHeaderStamp),
                    Long.toHexString(HEADER_PROTECTOR_STAMP),
                    Long.toHexString(delegateAddress)));
        }

        final long actualFooterStamp = Jvm.getLong(delegateAddress + HEADER_SIZE + externalSize);
        if (actualFooterStamp != FOOTER_PROTECTOR_STAMP) {
            throw new AllocatorException(String.format("Footer protection error: %s (must be %s) for addr %s",
                    Long.toHexString(actualFooterStamp),
                    Long.toHexString(FOOTER_PROTECTOR_STAMP),
                    Long.toHexString(delegateAddress)));
        }

        return externalSize;
    }
}
