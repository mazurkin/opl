package org.opl.memory.extra;

import org.opl.memory.Allocator;
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
 * </ul>
 */
public class DefensiveAllocatorProxy implements Allocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefensiveAllocatorProxy.class);

    private static final long HEADER_SIZE = Mem.LONG_SIZE + Mem.LONG_SIZE;

    private static final long FOOTER_SIZE = Mem.LONG_SIZE;

    private static final long HEADER_PROTECTOR_STAMP = 0xEF0123456789ABCDL;

    private static final long FOOTER_PROTECTOR_STAMP = 0xEDCBA9876543210FL;

    private final Allocator delegate;

    private final AtomicLong balance;

    /**
     * Construct a defensive proxy around delegate allocator
     * @param delegate Main allocator instance
     */
    public DefensiveAllocatorProxy(Allocator delegate) {
        this.delegate = delegate;
        this.balance = new AtomicLong(0);
    }

    @Override
    public long allocate(long size) {
        final long delegateSize = size + HEADER_SIZE + FOOTER_SIZE;
        final long delegateAddress = delegate.allocate(delegateSize);
        final long externalAddress = delegateAddress + HEADER_SIZE;

        balance.addAndGet(size);

        storeSize(delegateAddress, size);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes are allocated at {}", size, Long.toHexString(externalAddress));
        }

        return externalAddress;
    }

    @Override
    public long reallocate(long address, long newSize) {
        final long delegateAddress = address - HEADER_SIZE;
        final long curSize = fetchSize(delegateAddress);

        final long newDelegateSize = newSize + HEADER_SIZE + FOOTER_SIZE;
        final long newDelegateAddress = delegate.reallocate(delegateAddress, newDelegateSize);
        final long newExternalAddress = newDelegateAddress + HEADER_SIZE;

        balance.addAndGet(newSize - curSize);

        storeSize(newDelegateAddress, newSize);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes at {} are reallocated to {} bytes at {}",
                    curSize, Long.toHexString(address),
                    newSize, Long.toHexString(newExternalAddress));
        }

        return newExternalAddress;
    }

    @Override
    public void free(long address) {
        final long delegateAddress = address - HEADER_SIZE;
        final long size = fetchSize(delegateAddress);

        delegate.free(delegateAddress);

        balance.addAndGet(-size);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("{} bytes at {} are freed", size, Long.toHexString(address));
        }
    }

    public long getBalance() {
        return balance.get();
    }

    private static void storeSize(long delegateAddress, long size) {
        Jvm.putLong(delegateAddress, size);
        Jvm.putLong(delegateAddress + Mem.LONG_SIZE, HEADER_PROTECTOR_STAMP);
        Jvm.putLong(delegateAddress + HEADER_SIZE + size, FOOTER_PROTECTOR_STAMP);
    }

    private static long fetchSize(long delegateAddress) {
        final long size = Jvm.getLong(delegateAddress);

        final long actualHeaderStamp = Jvm.getLong(delegateAddress + Mem.LONG_SIZE);
        if (actualHeaderStamp != HEADER_PROTECTOR_STAMP) {
            throw new IllegalStateException(String.format("Header protection error: %s (must be %s) for addr %s",
                    Long.toHexString(actualHeaderStamp),
                    Long.toHexString(HEADER_PROTECTOR_STAMP),
                    Long.toHexString(delegateAddress)
            ));
        }

        final long actualFooterStamp = Jvm.getLong(delegateAddress + HEADER_SIZE + size);
        if (actualFooterStamp != FOOTER_PROTECTOR_STAMP) {
            throw new IllegalStateException(String.format("Footer protection error: %s (must be %s) for addr %s",
                    Long.toHexString(actualFooterStamp),
                    Long.toHexString(FOOTER_PROTECTOR_STAMP),
                    Long.toHexString(delegateAddress)
            ));
        }

        return size;
    }

}
