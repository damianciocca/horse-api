package com.etermax.spacehorse.core.matchmaking.action;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.capitain.infrastructure.DynamoCaptain;
import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;

public class BattlePlayerFactory {
	static public BattlePlayer buildBattlePlayer(Player player, PlayerWinRate playerWinRate, CaptainsCollection captainCollection) {
		BattlePlayer battlePlayer = new BattlePlayerBuilder() //
				.setUserId(player.getUserId()) //
				.setSelectedCards(shuffleCards(player.getDeck().getSelectedCards())) //
				.setMmr(playerWinRate.getMmr()) //
				.setBotMmr(player.getBotMmr()) //
				.setCatalogId(player.getCatalogId()) //
				.setAbTag(player.getAbTag().toString()) //
				.setName(player.getName()) //
				.setLevel(player.getProgress().getLevel()) //
				.setSelectedCaptainId(captainCollection.getSelectedCaptainId()) //
				.setCaptainSlots(getCaptainSlotsOfSkins(captainCollection)) //
				.createBattlePlayer();

		if (battlePlayer.getSelectedCards().size() < 5) {
			throw new ApiException(
					"Invalid number of cards in player " + player.getUserId() + ", it has " + battlePlayer.getSelectedCards().size() + " cards");
		}

		return battlePlayer;
	}

	static private List<DynamoCaptainSlot> getCaptainSlotsOfSkins(CaptainsCollection captainCollection) {
		return captainCollection.getCaptainSlotsOfSelectedCaptain().stream().map(DynamoCaptain::newDynamoCaptainSlot).collect(Collectors.toList());
	}

	static private List<Card> shuffleCards(List<Card> selectedCards) {
		Collections.shuffle(selectedCards, ThreadLocalRandom.current());
		return selectedCards;
	}
}
