package org.opl.alg.bitset;

public interface OplBitSet extends AutoCloseable {

    void set(long bit);

    void toggle(long bit);

    void clean(long bit);

    boolean get(long bit);

    void reset();

    long size();
}
