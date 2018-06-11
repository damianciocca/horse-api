package com.etermax.spacehorse.core.catalog.model.captain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class CaptainDefinition extends CatalogEntry {

	private final int mapNumber;
	private final int goldPrice;
	private final int gemsPrice;

	public CaptainDefinition(String id, int mapNumber, int goldPrice, int gemsPrice) {
		super(id);
		this.mapNumber = mapNumber;
		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(mapNumber >= 0, "map number < 0");

		if (gemsPrice <= 0 && goldPrice <= 0) {
			throw new CatalogException("gems or gold should be greater than zero for captain id " + getId());
		}

		Map<String, List<CaptainSkinDefinition>> skinsByCaptainIds = getSkinsGroupedByCaptainIds(catalog);

		if (!skinsByCaptainIds.containsKey(getId())) {
			throw new CatalogException("Missing default skins for captain id " + getId());
		}

		if (skinsByCaptainIds.get(getId()).stream().filter(x -> x.isDefault()).count() != CaptainSlot.MAX_SLOT_NUMBER) {
			throw new CatalogException(
					"Missing default skins for captain id " + getId() + ", it must have skins for " + CaptainSlot.MAX_SLOT_NUMBER + " slots");
		}
	}

	private Map<String, List<CaptainSkinDefinition>> getSkinsGroupedByCaptainIds(Catalog catalog) {
		return catalog.getCaptainSkinDefinitionsCollection().getEntries().stream()
				.collect(Collectors.groupingBy(CaptainSkinDefinition::getCaptainId));
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getGoldPrice() {
		return goldPrice;
	}

	public int getGemsPrice() {
		return gemsPrice;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
