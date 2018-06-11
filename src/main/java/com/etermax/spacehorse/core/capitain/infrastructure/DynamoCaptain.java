package com.etermax.spacehorse.core.capitain.infrastructure;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.capitain.infrastructure.skins.DynamoCaptainSkin;
import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@DynamoDBDocument
public class DynamoCaptain {

	@DynamoDBAttribute(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "ownedSkins")
	private List<DynamoCaptainSkin> ownedSkins;

	@DynamoDBAttribute(attributeName = "captainSlots")
	private List<DynamoCaptainSlot> captainSlots;

	public DynamoCaptain() {
		// just for dynamo mapper
	}

	public DynamoCaptain(String id, List<DynamoCaptainSkin> ownedSkins, List<DynamoCaptainSlot> captainSlots) {
		this.id = id;
		this.ownedSkins = ownedSkins;
		this.captainSlots = captainSlots;
	}

	public static Captain mapToCaptain(DynamoCaptain dynamoCaptain, Catalog catalog) {

		CaptainDefinition captainDefinition = catalog.getCaptainDefinitionsCollection().findByIdOrFail(dynamoCaptain.getId());

		Map<String, CaptainSkin> ownedSkins = Maps.newHashMap();
		if (dynamoCaptain.getOwnedSkins() != null) {
			ownedSkins = dynamoCaptain.getOwnedSkins().stream().collect(toMap(DynamoCaptainSkin::getId, toCaptainSkin(catalog)));
		}
		Map<Integer, CaptainSlot> captainSlots = Maps.newHashMap();
		if (dynamoCaptain.getOwnedSkins() != null) {
			captainSlots = dynamoCaptain.getCaptainSlots().stream().collect(toMap(DynamoCaptainSlot::getSlotNumber, toCaptainSlot(catalog)));
		}

		return new Captain(dynamoCaptain.getId(), captainDefinition, ownedSkins, captainSlots);
	}

	public static DynamoCaptain mapToDynamoCaptain(Captain captain) {

		List<DynamoCaptainSkin> dynamoOwnedSkinsByIds = Lists.newArrayList();
		if (captain.getOwnedSkins() != null) {
			dynamoOwnedSkinsByIds = captain.getOwnedSkins().stream().map(DynamoCaptain::newDynamoCaptainSkin).collect(Collectors.toList());
		}
		List<DynamoCaptainSlot> dynamoCaptainSlots = Lists.newArrayList();
		if (captain.getCaptainSlots() != null) {
			dynamoCaptainSlots = captain.getCaptainSlots().stream().map(DynamoCaptain::newDynamoCaptainSlot).collect(Collectors.toList());
		}

		return new DynamoCaptain(captain.getCaptainId(), dynamoOwnedSkinsByIds, dynamoCaptainSlots);
	}

	public static DynamoCaptainSlot newDynamoCaptainSlot(CaptainSlot captainSlot) {
		return new DynamoCaptainSlot(captainSlot.getSlotNumber(), new DynamoCaptainSkin(captainSlot.getCaptainSkinId()));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DynamoCaptainSkin> getOwnedSkins() {
		return ownedSkins;
	}

	public void setOwnedSkins(List<DynamoCaptainSkin> ownedSkins) {
		this.ownedSkins = ownedSkins;
	}

	public List<DynamoCaptainSlot> getCaptainSlots() {
		return captainSlots;
	}

	public void setCaptainSlots(List<DynamoCaptainSlot> captainSlots) {
		this.captainSlots = captainSlots;
	}

	private static DynamoCaptainSkin newDynamoCaptainSkin(CaptainSkin captainSkin) {
		return new DynamoCaptainSkin(captainSkin.getCaptainSkinId());
	}

	private static Function<DynamoCaptainSkin, CaptainSkin> toCaptainSkin(Catalog catalog) {
		return dynamoCaptainSkin -> {
			CaptainSkinDefinition captainSkinDefinition = catalog.getCaptainSkinDefinitionsCollection().findByIdOrFail(dynamoCaptainSkin.getId());
			return dynamoCaptainSkin.toCaptainSkin(captainSkinDefinition);
		};
	}

	private static Function<DynamoCaptainSlot, CaptainSlot> toCaptainSlot(Catalog catalog) {
		return dynamoCaptainSlot -> {
			CaptainSkinDefinition captainSkinDefinition = catalog.getCaptainSkinDefinitionsCollection()
					.findByIdOrFail(dynamoCaptainSlot.getCaptainSkinId());
			return dynamoCaptainSlot.toCaptainSlot(dynamoCaptainSlot, captainSkinDefinition);
		};
	}

}
