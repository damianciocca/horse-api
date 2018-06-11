package com.etermax.spacehorse.core.login.action;

import java.util.Optional;

import com.etermax.spacehorse.core.login.model.GooglePlayValidator;
import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.user.model.Platform;

public class LinkGooglePlayAction {

	private final PlayerRepository playerRepository;

	private final AuthRepository authRepository;

	private final GooglePlayValidator googlePlayValidator;

	public LinkGooglePlayAction(PlayerRepository playerRepository, AuthRepository authRepository) {
		this.playerRepository = playerRepository;
		this.authRepository = authRepository;
		// TODO: Retrieve GooglePlayValidator params from resource properties file (in constructor params)
		this.googlePlayValidator = new GooglePlayValidator();
	}

	public LinkGooglePlayAction(PlayerRepository playerRepository, AuthRepository authRepository,
			GooglePlayValidator googlePlayValidator) {
		this.playerRepository = playerRepository;
		this.authRepository = authRepository;
		this.googlePlayValidator = googlePlayValidator;
	}

	public String linkWithPlayServices(String token, String loginId) {
		String googlePlayId = googlePlayValidator.getGPGSIdWhenTokenIsVerified(token);
		return authRepository.link(Platform.ANDROID, loginId, googlePlayId);
	}

	public Player getPlayerFromPlayToken(String token) {
		String googlePlayId = googlePlayValidator.getGPGSIdWhenTokenIsVerified(token);
		String loginId = authRepository.findById(Platform.ANDROID, googlePlayId);
		if (loginId == null || loginId.isEmpty()) {
			return null;
		}
		return playerRepository.find(loginId);
	}

	public Optional<String> findGooglePlayIdLinkedWithLoginId(String loginId) {
		String googlePlayId = authRepository.findByUserId(Platform.ANDROID, loginId);

		if (googlePlayId == null || googlePlayId.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(googlePlayId);
	}

	public void deleteLinkWithPlayerServices(String loginId) {
		authRepository.delete(Platform.ANDROID, loginId);
	}
}
