package com.etermax.spacehorse.core.capitain.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkinFactory;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

public class CaptainFactory {

	private final CaptainSkinFactory captainSkinFactory;

	public CaptainFactory() {
		captainSkinFactory = new CaptainSkinFactory();
	}

	public Captain createWithDefaultSkin(String captainId, CaptainDefinition captainDefinition, List<CaptainSkinDefinition> captainSkinDefinitions) {
		Map<String, CaptainSkin> ownedCaptainSkins = createDefaultSkins(captainId, captainSkinDefinitions);
		Map<Integer, CaptainSlot> captainSlots = createDefaultCaptainSlots(captainId, captainSkinDefinitions);
		return new Captain(captainId, captainDefinition, ownedCaptainSkins, captainSlots);
	}

	public Captain createWithRandomSkin(String captainId, CaptainDefinition captainDefinition, List<CaptainSkinDefinition> captainSkinDefinitions) {
		List<CaptainSkinDefinition> captainSkinDefinitionsFiltered = filterCaptainSkinDefinitionsFor(captainId, captainSkinDefinitions);
		List<CaptainSkinDefinition> captainSkinDefinitionsWithoutRacingJadeSkin = removeJadeRacingSkinDefinition(captainSkinDefinitionsFiltered);
		List<CaptainSkinDefinition> captainSkins = createRandomSkins(captainSkinDefinitionsWithoutRacingJadeSkin);
		Map<String, CaptainSkin> ownedCaptainSkins = createCaptainSkinMap(captainSkins);
		Map<Integer, CaptainSlot> captainSlots = createCaptainSlotMap(captainSkins);
		return new Captain(captainId, captainDefinition, ownedCaptainSkins, captainSlots);
	}

	public Map<String, CaptainSkin> createDefaultSkins(String captainId, List<CaptainSkinDefinition> captainSkinDefinitions) {
		List<CaptainSkinDefinition> captainSkins = getDefaultCaptainSkinDefinitions(captainId, captainSkinDefinitions);
		return createCaptainSkinMap(captainSkins);
	}

	public Map<Integer, CaptainSlot> createDefaultCaptainSlots(String captainId, List<CaptainSkinDefinition> captainSkinDefinitions) {
		List<CaptainSkinDefinition> captainSkins = getDefaultCaptainSkinDefinitions(captainId, captainSkinDefinitions);
		return createCaptainSlotMap(captainSkins);
	}

	private List<CaptainSkinDefinition> removeJadeRacingSkinDefinition(List<CaptainSkinDefinition> captainSkinDefinitions) {
		captainSkinDefinitions.removeIf(this::isJadeRacingSkin);
		return captainSkinDefinitions;
	}

	private boolean isJadeRacingSkin(CaptainSkinDefinition captainSkinDefinition) {
		return captainSkinDefinition.getCaptainId().equals("captain_jade") &&
				captainSkinDefinition.getSkinId().equals("racing");
	}

	private List<CaptainSkinDefinition> filterCaptainSkinDefinitionsFor(String captainId, List<CaptainSkinDefinition> captainSkinDefinitions) {
		return captainSkinDefinitions.stream().filter(captainSkinDefinition -> captainSkinDefinition.getCaptainId().equals(captainId))
				.collect(Collectors.toList());
	}

	private Map<String, CaptainSkin> createCaptainSkinMap(List<CaptainSkinDefinition> captainSkins) {
		return captainSkins.stream().map(toCaptainSkin()).collect(Collectors.toMap(CaptainSkin::getCaptainSkinId, captainSkin -> captainSkin));
	}

	private Map<Integer, CaptainSlot> createCaptainSlotMap(List<CaptainSkinDefinition> captainSkins) {
		return captainSkins.stream().map(toCaptainSlot()).collect(Collectors.toMap(CaptainSlot::getSlotNumber, captainSlot -> captainSlot));
	}

	private List<CaptainSkinDefinition> createRandomSkins(List<CaptainSkinDefinition> captainSkinDefinitions) {
		int randomSkimIndex = ThreadLocalRandom.current().nextInt(captainSkinDefinitions.size());
		String skinId = captainSkinDefinitions.get(randomSkimIndex).getSkinId();
		return captainSkinDefinitions.stream().filter(captainSkinDefinition -> captainSkinDefinition.getSkinId().equals(skinId))
				.collect(Collectors.toList());
	}

	private List<CaptainSkinDefinition> getDefaultCaptainSkinDefinitions(String captainId, List<CaptainSkinDefinition> skinDefinitions) {
		return skinDefinitions.stream().filter(byDefaultSkinsOf(captainId)).collect(Collectors.toList());
	}

	private Function<CaptainSkinDefinition, CaptainSlot> toCaptainSlot() {
		return captainSkinDefinition -> new CaptainSlot(captainSkinDefinition.getSlotNumber(),
				captainSkinFactory.create(captainSkinDefinition.getId(), captainSkinDefinition));
	}

	private Function<CaptainSkinDefinition, CaptainSkin> toCaptainSkin() {
		return captainSkinDefinition -> captainSkinFactory.create(captainSkinDefinition.getId(), captainSkinDefinition);
	}

	private Predicate<CaptainSkinDefinition> byDefaultSkinsOf(String captainId) {
		return captainSkinDefinition -> captainSkinDefinition.isDefault() && captainId.equals(captainSkinDefinition.getCaptainId());
	}
}
