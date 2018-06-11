package com.etermax.spacehorse.core.player.action;

import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.model.PlayerInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerAction {

	private final PlayerRepository playerRepository;

	private final CatalogRepository catalogRepository;

	public PlayerAction(PlayerRepository playerRepository, CatalogRepository catalogRepository) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
	}

	public List<PlayerInfo> findPlayersInfo(List<String> playerIds) {
		return playerRepository.find(playerIds).stream().map(player -> new PlayerInfo(player.getUserId(), player.getName())).collect(Collectors.toList());
	}

	public Optional<Player> findByLoginId(String loginId) {
		Player player = playerRepository.find(loginId);
		return Optional.ofNullable(player);
	}

	public Player findByLoginIdOrFail(String loginId) {
		Optional<Player> playerOpt = this.findByLoginId(loginId);
		if (!playerOpt.isPresent()) {
			throw new ApiException("Player loginId not found: " + loginId);
		}
		return playerOpt.get();
	}

    public void updatePlayerProfile(Player player, String name, Gender gender, int age) {
		player.setName(name);
		if (!name.equals(player.getName())) {
			player.getPlayerStats().incrementNameChanges();
		}
		player.setGender(gender);
		player.setAge(age);
		playerRepository.update(player);
    }

	public void startTutorial(Player player, String tutorialId) {
		player.validateTutorialProgress(tutorialId);
		if(player.isTutorialIdActive(tutorialId)) {
			return;
		}
		boolean notExistsTutorialId = catalogRepository.getActiveCatalogWithTag(player.getAbTag())
				.getTutorialProgressCollection()
				.getEntries().stream()
				.noneMatch(tutorialProgressEntry -> tutorialProgressEntry.getId()
						.equals(tutorialId));
		if (notExistsTutorialId) {
			throw new TutorialException("TutorialId doesn't exist");
		}
		player.setActiveTutorial(tutorialId);
		playerRepository.update(player);
	}

	public void finishActiveTutorial(Player player) {
		if(!player.hasActiveTutorial()) {
			throw new TutorialException("Player hasn't an active tutorial.");
		}
		player.finishActiveTutorial();
		playerRepository.update(player);
	}
}
