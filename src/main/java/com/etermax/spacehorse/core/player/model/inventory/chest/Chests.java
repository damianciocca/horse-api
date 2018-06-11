package com.etermax.spacehorse.core.player.model.inventory.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotsInspector;

@DynamoDBDocument
public class Chests {

	@DynamoDBAttribute(attributeName = "chests")
	private List<Chest> chests;

	public Chests() {
		chests = new ArrayList<>();
	}

	public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}

	public Chest addChest(String chestType, int mapNumber, ChestConstants chestConstants, int playerLevel, ChestSlotsInspector chestSlotsInspector) {
		if (!canAddChest(playerLevel, chestSlotsInspector)) {
			return null;
		}
		Chest chest = new Chest(getNextChestId(), chestType, mapNumber, getNextSlotNumber(chestConstants));
		chests.add(chest);
		return chest;
	}

	public boolean canAddChest(int playerLevel, ChestSlotsInspector chestSlotsInspector) {
		return chestSlotsInspector.hasAvailable(playerLevel, this);
	}

	public void removeChest(Chest chest) {
		chests.removeIf(c -> c.getId().equals(chest.getId()));
	}

	private Long getNextChestId() {
		OptionalLong optionalMax = getChests().stream().mapToLong(Chest::getId).max();
		if (optionalMax.isPresent()) {
			return optionalMax.getAsLong() + 1;
		} else {
			return 1L;
		}
	}

	public Integer getNextSlotNumber(ChestConstants chestConstants) {
		for (int i = 0; i < chestConstants.getMaxChests(); i++) {
			if (!isThereAChestInSlotNumber(i)) {
				return i;
			}
		}

		return 0;
	}

	private boolean isThereAChestInSlotNumber(int slotNumber) {
		return chests.stream().anyMatch(x -> x.getSlotNumber() != null && x.getSlotNumber() == slotNumber);
	}

	public Optional<Chest> findChestById(Long id) {
		return getChests().stream().filter(chest -> chest.getId().equals(id)).findFirst();
	}

	public void checkIntegrity(Catalog catalog) {
		chests.removeIf(chest -> !hasValidChestType(catalog, chest));
		chests.forEach(chest -> chest.checkIntegrity(catalog, this));
	}

	static private boolean hasValidChestType(Catalog catalog, Chest chest) {
		return catalog.getChestDefinitionsCollection().findById(chest.getChestType()).isPresent();
	}
}
