package com.etermax.spacehorse.core.matchmaking.model.selectors.captains;

import java.util.Optional;

import com.etermax.spacehorse.core.capitain.model.Captain;

public class NullCaptainSelectorStrategy implements CaptainSelectorStrategy {

	@Override
	public Optional<Captain> chooseRandomBotCaptain(int botMmr) {
		return Optional.empty();
	}
}

