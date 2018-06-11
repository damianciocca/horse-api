package com.etermax.spacehorse.core.catalog.model.captain;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class CaptainSkinDefinition extends CatalogEntry {

	private final String captainId;
	private final String skinId;
	private final Slot slot;
	private final int goldPrice;
	private final int gemsPrice;
	private final boolean isDefault;

	public CaptainSkinDefinition(String id, String captainId, String skinId, Slot slot, int goldPrice, int gemsPrice, boolean isDefault) {
		super(id);
		this.captainId = captainId;
		this.skinId = skinId;
		this.slot = slot;
		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
		this.isDefault = isDefault;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(isNotBlank(captainId), "captain id should not be blank");
		validateParameter(isNotBlank(skinId), "skin id should not be blank");
		validateParameter(slot != null, "slot should not null");
		validateParameter(slot.slotNumber >= 0, "slot number should be greater or equals than zero");

		validateParameter(slot.slotNumber < CaptainSlot.MAX_SLOT_NUMBER, "slot number should be less than " + CaptainSlot.MAX_SLOT_NUMBER);

		validateIfExistCaptainIdAsCaptain(catalog);

		if (isDefault())
			validateDuplicatedDefaultSkin(catalog);

		Map<String, List<CaptainSkinDefinition>> skinsByCaptainIds = getSkinsGroupedByCaptainIds(catalog);
		skinsByCaptainIds.forEach(this::validateDefaultSkinByCaptainAndSlot);
	}

	private void validateDuplicatedDefaultSkin(Catalog catalog) {
		catalog.getCaptainSkinDefinitionsCollection().getEntries().stream()
				.filter(skin -> skin.getCaptainId().equals(captainId) && skin.getSlotNumber() == getSlotNumber() && skin != this && skin
						.isDefault()).findFirst().ifPresent(skin -> {
					throw new CatalogException(
							format("Only one default captain skin is allowed per slot. Captain id [%s], slot number [%s]", captainId, getSlotNumber()));
				}

		);
	}

	public String getCaptainId() {
		return captainId;
	}

	public String getSkinId() {
		return skinId;
	}

	public Slot getSlot() {
		return slot;
	}

	public int getSlotNumber() {
		return slot.getSlotNumber();
	}

	public int getGoldPrice() {
		return goldPrice;
	}

	public int getGemsPrice() {
		return gemsPrice;
	}

	public boolean isDefault() {
		return isDefault;
	}

	private void validateDefaultSkinByCaptainAndSlot(String captainId, List<CaptainSkinDefinition> captainSkins) {

		Map<Integer, List<CaptainSkinDefinition>> skinsBySlotNumbers = captainSkins.stream().filter(skin -> skin.captainId.equals(captainId))
				.collect(Collectors.groupingBy(CaptainSkinDefinition::getSlotNumber));

		for (Map.Entry<Integer, List<CaptainSkinDefinition>> skinsBySlotNumber : skinsBySlotNumbers.entrySet()) {

			long skinsInSlot = skinsBySlotNumber.getValue().stream().filter(definition -> definition.isDefault).count();

			if (skinsInSlot > 1) {
				throw new CatalogException(format("Only one default captain skin is allowed per slot. Captain id [%s], slot number [%s]", captainId,
						skinsBySlotNumber.getKey()));
			}
		}
	}

	private Map<String, List<CaptainSkinDefinition>> getSkinsGroupedByCaptainIds(Catalog catalog) {
		return catalog.getCaptainSkinDefinitionsCollection().getEntries().stream()
				.collect(Collectors.groupingBy(CaptainSkinDefinition::getCaptainId));
	}

	private void validateIfExistCaptainIdAsCaptain(Catalog catalog) {
		catalog.getCaptainDefinitionsCollection().findByIdOrFail(captainId);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public static class Slot {

		private final int slotNumber;

		public Slot(int slotNumber) {
			this.slotNumber = slotNumber;
		}

		public int getSlotNumber() {
			return slotNumber;
		}
	}

}
