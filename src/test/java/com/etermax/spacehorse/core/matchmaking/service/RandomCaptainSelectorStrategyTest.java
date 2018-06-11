package com.etermax.spacehorse.core.matchmaking.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.matchmaking.model.selectors.captains.RandomCaptainSelectorStrategy;

public class RandomCaptainSelectorStrategyTest {

	private static final String CAPTAIN_REX = "captain-rex";
	private static final String CAPTAIN_HELA = "captain-hela";
	private static final String CAPTAIN_JADE = "captain-jade";

	private Collection<MapDefinition> mapDefinitions;
	private List<CaptainDefinition> captainDefinitions;
	private List<CaptainSkinDefinition> captainSkinDefinitions;

	@Before
	public void setUp() {
		mapDefinitions = aMapDefinitions();
		captainDefinitions = aCaptainDefinitions();
		captainSkinDefinitions = aCaptainSkinDefinitions();
	}

	@Test
	public void whenMmrIs100ThenTheCaptainRexShouldBeChosen() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(100).get().getCaptainId();
		// then
		assertThat(captainId).isEqualTo(CAPTAIN_REX);
	}

	@Test
	public void whenMmrIs200ThenTheCaptainRexShouldBeChosenAgain() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(100).get().getCaptainId();
		// then
		assertThat(captainId).isEqualTo(CAPTAIN_REX);
	}

	@Test
	public void whenMmrIs400ThenTheCaptainRexOrHelaShouldBeChosen() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(400).get().getCaptainId();
		// then
		assertThat(newArrayList(CAPTAIN_REX, CAPTAIN_HELA).contains(captainId)).isTrue();
	}

	@Test
	public void whenMmrIs500ThenTheCaptainRexOrHelaShouldBeChosenAgain() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(500).get().getCaptainId();
		// then
		assertThat(newArrayList(CAPTAIN_REX, CAPTAIN_HELA).contains(captainId)).isTrue();
	}

	@Test
	public void whenMmrIs700ThenTheCaptainRexOrHelaOrJadeShouldBeChosen() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(700).get().getCaptainId();
		// then
		assertThat(newArrayList(CAPTAIN_REX, CAPTAIN_HELA, CAPTAIN_JADE).contains(captainId)).isTrue();
	}

	@Test
	public void whenMmrIs800ThenTheCaptainRexOrHelaOrJadeShouldBeChosenAgain() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(800).get().getCaptainId();
		// then
		assertThat(newArrayList(CAPTAIN_REX, CAPTAIN_HELA, CAPTAIN_JADE).contains(captainId)).isTrue();
	}

	@Test
	public void whenTheSelectorCannotChooseAnyCaptainThenTheFirstCaptainDefinitionShouldBeChosen() {
		// given
		RandomCaptainSelectorStrategy selectorStrategy = new RandomCaptainSelectorStrategy(mapDefinitions, captainDefinitions,
				captainSkinDefinitions);
		// When
		String captainId = selectorStrategy.chooseRandomBotCaptain(50).get().getCaptainId();
		// then
		assertThat(captainId).isEqualTo(CAPTAIN_REX);
	}

	private List<CaptainDefinition> aCaptainDefinitions() {
		return newArrayList( //
				aCaptainDefinitions(CAPTAIN_REX, 1), //
				aCaptainDefinitions(CAPTAIN_HELA, 2), //
				aCaptainDefinitions(CAPTAIN_JADE, 3));
	}

	private Collection<MapDefinition> aMapDefinitions() {
		return newArrayList( //
				aMapDefinition("map1", 1, 100), //
				aMapDefinition("map2", 2, 400), //
				aMapDefinition("map3", 3, 700));
	}

	private MapDefinition aMapDefinition(String mapId, int mapNumber, int mmr) {
		return new MapDefinition(mapId, mapNumber, mmr, 1, 1);
	}

	private CaptainDefinition aCaptainDefinitions(String captainId, int mapNumber) {
		return new CaptainDefinition(captainId, mapNumber, 0, 100);
	}

	private List<CaptainSkinDefinition> aCaptainSkinDefinitions() {
		return newArrayList( //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_arms_0", "basic", 0, true), //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_chest_1", "basic", 1, true), //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_head_2", "basic", 2, true), //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_arms2_0", "poli", 0, false), //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_chest2_1", "poli", 1, false), //
				aCaptainSkinDefinitions("captain-rex", "captain_rex_rex_head2_2", "poli", 2, false), //

				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_arms_0", "basic", 0, true), //
				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_chest_1", "basic", 1, true), //
				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_head_2", "basic", 2, true), //
				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_arms2_0", "witch", 0, false), //
				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_chest2_1", "witch", 1, false), //
				aCaptainSkinDefinitions("captain-hela", "captain_hela_hela_head2_2", "witch", 2, false), //

				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_arms_0", "basic", 0, true), //
				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_chest_1", "basic", 1, true), //
				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_head_2", "basic", 2, true), //
				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_arms2_0", "moro", 0, false), //
				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_chest2_1", "moro", 1, false), //
				aCaptainSkinDefinitions("captain-jade", "captain_jade_jade_head2_2", "moro", 2, false));
	}

	private CaptainSkinDefinition aCaptainSkinDefinitions(String captainId, String id, String skinId, int slotNumber, boolean isDefault) {
		return new CaptainSkinDefinition(id, captainId, skinId, new CaptainSkinDefinition.Slot(slotNumber), 0, 0, isDefault);
	}

}
