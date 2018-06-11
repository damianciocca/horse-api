package com.etermax.spacehorse.core.catalog.model.bot;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class BotsChancesByMmrDefinition extends CatalogEntry {

	public static final int NO_CHANCE = 0;
	public static final int TOP_CHANCE = 100;

	private final int minMmr;
	private final int maxMmr;
	private final int chance;

	public BotsChancesByMmrDefinition(String id, int minMmr, int maxMmr, int chance) {
		super(id);
		this.minMmr = minMmr;
		this.maxMmr = maxMmr;
		this.chance = chance;
	}

	public int getMinMmr() {
		return minMmr;
	}

	public int getMaxMmr() {
		return maxMmr;
	}

	public int getChance() {
		return chance;
	}

	public boolean isBetween(int mmr) {
		return mmr >= getMinMmr() && mmr <= getMaxMmr();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(minMmr >= NO_CHANCE, "min mmr < 0");
		validateParameter(maxMmr >= NO_CHANCE, "max mmr < 0");
		validateParameter(minMmr < maxMmr, "min mmr should be less than max mmr");
		validateParameter(chance >= NO_CHANCE && chance <= TOP_CHANCE, "chance should be between 0 and 100");
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
