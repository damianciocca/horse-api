package com.etermax.spacehorse.core.player.model.progress;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequenceTest {

    @Test
    public void testRegularSequence() {
        Assert.assertEquals(5, Sequence.calculateProportionalSequence(5, 10));
    }

	@Test
	public void testFirstPosition() {
		assertEquals(0, Sequence.calculateProportionalSequence(0, 10));
	}

	@Test
	public void testLastPosition() {
		assertEquals(9, Sequence.calculateProportionalSequence(9, 10));
	}

	@Test
	public void testLastPositionPlusOne() {
		assertEquals(0, Sequence.calculateProportionalSequence(10, 10));
	}

    @Test
    public void testBiggerSequenceThanSize() {
        assertEquals(5, Sequence.calculateProportionalSequence(25, 10));
    }

    @Test
    public void testBorderCase() {
        assertEquals(0, Sequence.calculateProportionalSequence(10, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSizeCannotBeNegative() {
        Sequence.calculateProportionalSequence(10, -1);
    }

    @Test
    public void testEqualsAndHashCode() {
        int sequenceOrder = 7;
        Sequence x = new Sequence(sequenceOrder);
        Sequence y = new Sequence(sequenceOrder);
        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    public void testRoundingNumbersCase() {
        int sequenceOrder = 13;
        int sequenceSize = 45;
        Sequence sequence = new Sequence(sequenceOrder);
        sequence.scaleOrder(sequenceSize);
        assertEquals(sequenceOrder, sequence.getOrder());
    }

    @Test
    public void testCycle() {
        int sequenceSize = 45;
        Sequence sequence = new Sequence();
        for (int j = 0; j < sequenceSize; j ++) {
            for (int i = 0; i < sequenceSize; i++) {
                sequence.scaleOrder(sequenceSize);
                Assert.assertEquals(i, sequence.getOrder());
                sequence.increaseOrder();
            }
        }
    }

}
