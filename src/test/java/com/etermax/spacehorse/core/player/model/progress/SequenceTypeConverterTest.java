package com.etermax.spacehorse.core.player.model.progress;

import org.junit.Assert;
import org.junit.Test;

public class SequenceTypeConverterTest {

    private SequenceTypeConverter sequenceTypeConverter = new SequenceTypeConverter();

    @Test
    public void testConvert() {
        Integer order = 7;
        Assert.assertEquals(order, sequenceTypeConverter.convert(new Sequence(order)));
    }

    @Test
    public void testUnConvert() {
        Integer order = 7;
        Assert.assertEquals(new Sequence(order), sequenceTypeConverter.unconvert(order));
    }

}
