package com.etermax.spacehorse.core.matchmaking.model.selectors.captains;

import java.util.Optional;

import com.etermax.spacehorse.core.capitain.model.Captain;

public interface CaptainSelectorStrategy {

	Optional<Captain> chooseRandomBotCaptain(int botMmr);
}
