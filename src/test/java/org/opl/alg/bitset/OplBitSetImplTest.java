package org.opl.alg.bitset;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opl.access.block.MemoryBlockFactory;
import org.opl.allocator.Allocators;
import org.opl.util.OplUtils;

public class OplBitSetImplTest {

    private MemoryBlockFactory factory;

    private OplBitSet set;

    @Before
    public void setUp() throws Exception {
        factory = Allocators.createTestMemoryBlockFactory();
        set = new OplBitSetImpl(factory, 100 * 1024);
    }

    @After
    public void tearDown() throws Exception {
        OplUtils.closeQuietly(set);
        OplUtils.closeQuietly(factory);
    }

    @Test
    public void operations() throws Exception {
        set.set(10001);
        Assert.assertFalse(set.get(10000));
        Assert.assertFalse(set.get(10002));
        Assert.assertTrue(set.get(10001));

        set.toggle(10002);
        Assert.assertFalse(set.get(10000));
        Assert.assertTrue(set.get(10002));
        Assert.assertTrue(set.get(10001));

        set.clean(10002);
        Assert.assertFalse(set.get(10000));
        Assert.assertFalse(set.get(10002));
        Assert.assertTrue(set.get(10001));
    }

    @Test
    public void last() throws Exception {
        long index = set.size() - 1;
        Assert.assertFalse(set.get(index));

        set.set(index);
        Assert.assertTrue(set.get(index));

        set.toggle(index);
        Assert.assertFalse(set.get(index));
    }
}
