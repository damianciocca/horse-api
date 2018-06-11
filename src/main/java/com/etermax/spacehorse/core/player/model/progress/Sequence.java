package com.etermax.spacehorse.core.player.model.progress;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Sequence {

    public static final int DEFAULT_START_VALUE = 0;

    private int order;

    public Sequence() {
        this(DEFAULT_START_VALUE);
    }

    public Sequence(int order) {
        this.setOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void scaleOrder(int sequenceSize) {
        this.order = calculateProportionalSequence(this.order, sequenceSize);
    }

    public void increaseOrder() {
        this.order++;
    }

    /**
     * @param sequenceOrder Sequence Order
     * @param sequenceSize  Sequence Size
     * @return The sequence order if it is lower than the sequence size,
     * otherwise it returns the proportional sequence according to the sequence size
     */
    public static int calculateProportionalSequence(int sequenceOrder, int sequenceSize) {
        Validate.isTrue(sequenceSize >= 0);
        return (int) Math.round(((double) sequenceOrder / (double) sequenceSize) % 1.0 * (double) sequenceSize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sequence sequence = (Sequence) o;
        return new EqualsBuilder().append(order, sequence.order).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(order).toHashCode();
    }

}
