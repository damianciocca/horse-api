package com.etermax.spacehorse.core.socialplayer.action;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.socialplayer.model.SocialPlayer;

public class GetSocialPlayerAction {
	private final PlayerRepository playerRepository;
	private final PlayerWinRateRepository playerWinRateRepository;
	private final CaptainCollectionRepository captainCollectionRepository;

	public GetSocialPlayerAction(PlayerRepository playerRepository, PlayerWinRateRepository playerWinRateRepository, CaptainCollectionRepository captainCollectionRepository) {
		this.playerRepository = playerRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.captainCollectionRepository = captainCollectionRepository;
	}

	public Optional<SocialPlayer> getSocialPlayer(String userId) {
		Optional<Player> playerOpt = Optional.ofNullable(playerRepository.find(userId));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			PlayerWinRate playerWinRate = playerWinRateRepository.findOrCrateDefault(player.getUserId());
			CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
			return Optional.of(new SocialPlayer(player, playerWinRate.getMmr(), captainsCollection.getSelectedCaptainId()));
		}

		return Optional.empty();
	}
}
