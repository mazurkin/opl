package org.opl.alg.bloom;

import org.opl.access.block.MemoryBlockFactory;
import org.opl.alg.bitset.OplBitSet;
import org.opl.alg.bitset.OplBitSetImpl;
import org.opl.util.OplUtils;

public class OplBloomFilterImpl implements OplBloomFilter, AutoCloseable {

    private final OplBitSet set;

    private final int countOfHashes;

    public OplBloomFilterImpl(MemoryBlockFactory factory, long size, int countOfHashes) {
        OplUtils.checkNotNull(factory, "Memory block factory is not specified");
        OplUtils.checkGreaterThanZero(size, "Size must be greater than 0");
        OplUtils.checkGreaterThanZero(countOfHashes, "Count of hashed must be greater than 0");

        this.set = new OplBitSetImpl(factory, size);
        this.countOfHashes = 0;
    }

    @Override
    public void close() throws Exception {
        set.close();
    }

    @Override
    public void clean() {
        set.reset();
    }

    @Override
    public boolean contains(long value) {
        for (int i = 0; i < countOfHashes; i++) {
            long bitIndex = bitIndex(value, i);
            if (!set.get(bitIndex)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void put(long value) {
        for (int i = 0; i < countOfHashes; i++) {
            long bitIndex = bitIndex(value, i);
            set.set(bitIndex);
        }
    }

    private static long bitIndex(long value, int index) {
        return 0;
    }
}
