package org.opl.alg.bloom;

public interface OplBloomFilter extends AutoCloseable {

    void put(long value);

    boolean contains(long value);

    void clean();

}
