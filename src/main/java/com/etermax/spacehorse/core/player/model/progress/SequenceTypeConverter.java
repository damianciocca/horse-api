package com.etermax.spacehorse.core.player.model.progress;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class SequenceTypeConverter implements DynamoDBTypeConverter<Integer, Sequence> {

    @Override
    public Integer convert(Sequence sequence) {
        return sequence.getOrder();
    }

    @Override
    public Sequence unconvert(Integer order) {
        Sequence sequence = new Sequence();
        sequence.setOrder(order);
        return sequence;
    }

}