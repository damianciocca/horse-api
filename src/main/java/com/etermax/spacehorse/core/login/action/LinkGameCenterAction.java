package com.etermax.spacehorse.core.login.action;

import java.util.Optional;

import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.user.model.Platform;

public class LinkGameCenterAction {

	private final PlayerRepository playerRepository;

	private final AuthRepository authRepository;

	public LinkGameCenterAction(PlayerRepository playerRepository, AuthRepository authRepository) {
		this.playerRepository = playerRepository;
		this.authRepository = authRepository;
	}

	public String linkWithGameCenter(String gameCenterId, String loginId) {
		return authRepository.link(Platform.IOS, loginId, gameCenterId);
	}

	public Player getPlayerFromPlayGameCenterId(String gameCenterId) {
		String loginId = authRepository.findById(Platform.IOS, gameCenterId);
		if (loginId == null || loginId.isEmpty()) {
			return null;
		}
		return playerRepository.find(loginId);
	}

	public Optional<String> findGameCenterIdLinkedWithLoginId(String loginId) {
		String gameCenterId = authRepository.findByUserId(Platform.IOS, loginId);

		if (gameCenterId == null || gameCenterId.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(gameCenterId);
	}

	public void deleteLinkWithGameCenter(String loginId) {
		authRepository.delete(Platform.IOS, loginId);
	}
}
