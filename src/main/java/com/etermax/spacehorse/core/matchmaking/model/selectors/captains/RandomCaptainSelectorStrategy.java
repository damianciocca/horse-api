package com.etermax.spacehorse.core.matchmaking.model.selectors.captains;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainFactory;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

public class RandomCaptainSelectorStrategy implements CaptainSelectorStrategy {

	private static final int FIRST_INDEX = 0;

	private final Collection<MapDefinition> mapDefinitions;
	private final List<CaptainDefinition> captainDefinitions;
	private final List<CaptainSkinDefinition> captainSkinDefinitions;
	private final CaptainFactory captainFactory;

	public RandomCaptainSelectorStrategy(Collection<MapDefinition> mapDefinitions, List<CaptainDefinition> captainDefinitions,
			List<CaptainSkinDefinition> captainSkinDefinitions) {
		this.mapDefinitions = mapDefinitions;
		this.captainDefinitions = captainDefinitions;
		this.captainSkinDefinitions = captainSkinDefinitions;
		this.captainFactory = new CaptainFactory();
	}

	public Optional<Captain> chooseRandomBotCaptain(int botMmr) {
		List<Integer> availableMapsNumbers = mapDefinitions.stream().filter(byBotMmr(botMmr)).map(MapDefinition::getMapNumber)
				.collect(Collectors.toList());
		List<CaptainDefinition> availableCaptainsDefinitions = captainDefinitions.stream().filter(byCaptainBelongsTo(availableMapsNumbers))
				.collect(Collectors.toList());
		Collections.shuffle(availableCaptainsDefinitions, ThreadLocalRandom.current());
		CaptainDefinition captainDefinition = availableCaptainsDefinitions.stream().findFirst().orElse(captainDefinitions.get(FIRST_INDEX));
		return Optional.of(captainFactory.createWithRandomSkin(captainDefinition.getId(), captainDefinition, captainSkinDefinitions));
	}

	private Predicate<CaptainDefinition> byCaptainBelongsTo(List<Integer> availableMapsNumbers) {
		return captainDefinition -> availableMapsNumbers.contains(captainDefinition.getMapNumber());
	}

	private Predicate<MapDefinition> byBotMmr(int botMmr) {
		return mapDefinition -> mapDefinition.getMmr() <= botMmr;
	}
}
