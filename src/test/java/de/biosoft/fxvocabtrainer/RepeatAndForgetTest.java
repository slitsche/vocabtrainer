package de.biosoft.fxvocabtrainer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RepeatAndForgetTest {

    RepeatAndForget raf = new RepeatAndForget();

    @Before
    public void setup() {
        List<LearnItem> items = new ArrayList<LearnItem>();
        items.add(new LearnItem("a", "a", 15));
        items.add(new LearnItem("b", "b", 7));
        items.add(new LearnItem("c", "c", 3));
        items.add(new LearnItem("d", "d", 1));
        raf.init(items);
    }

    @Test
    public void testInit() {

        assertEquals("wrong number of items after init", 4, raf.getCount());
        assertEquals("wrong number of items after init", 1,
                raf.getCount(RepeatAndForget.NEW));
        assertEquals("wrong number of items after init", 1,
                raf.getCount(RepeatAndForget.GOOD));
        assertEquals("wrong number of items after init", 1,
                raf.getCount(RepeatAndForget.FRESH));
        assertEquals("wrong number of items after init", 1,
                raf.getCount(RepeatAndForget.SOLID));
    }

    @Test
    public void testPoll() {
        LearnItem l = raf.next();
        assertNotNull(l);
    }

    @Test
    public void testAddItem() {
        LearnItem l = raf.next();
        int count = raf.getCount(l.getBucket());
        raf.add(l);
        assertEquals(count + 1, raf.getCount(l.getBucket()));
    }

    @Test
    public void testDistribution() {
        int[] buckets = { 0, 0, 0, 0 };
        final double total = 10000.0;

        for (int i = 0; i < total; i++) {
            int b = raf.getCurrentBucket();
            buckets[b] += 1;
        }

        assertEquals(0.55, buckets[3] / total, 0.01);
        assertEquals(0.25, buckets[2] / total, 0.01);
        assertEquals(0.15, buckets[1] / total, 0.01);
        assertEquals(0.05, buckets[0] / total, 0.01);
    }
}
