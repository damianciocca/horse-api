package com.etermax.spacehorse.core.socialplayer.model;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;

public class SocialPlayer {

	private String userId;
	private String name;
	private int mmr;
	private int mapNumber;
	private Deck deck;
	private PlayerProgress progress;
	private PlayerStats playerStats;
	private String selectedCaptainId;

	public SocialPlayer(Player player, int mmr, String selectedCaptainId) {
		this.userId = player.getUserId();
		this.name = player.getName();
		this.mmr = mmr;
		this.mapNumber = player.getMapNumber();
		this.deck = player.getDeck();
		this.progress = player.getProgress();
		this.playerStats = player.getPlayerStats();
		this.selectedCaptainId = selectedCaptainId;
	}

	public SocialPlayer(String userId, String name, Deck deck, PlayerProgress progress, PlayerStats playerStats, int mmr, int mapNumber, String selectedCaptainId) {
		this.userId = userId;
		this.name = name;
		this.deck = deck;
		this.progress = progress;
		this.playerStats = playerStats;
		this.mmr = mmr;
		this.mapNumber = mapNumber;
		this.selectedCaptainId = selectedCaptainId;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public Deck getDeck() {
		return deck;
	}

	public PlayerProgress getProgress() {
		return progress;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public int getMmr() {
		return mmr;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public String getSelectedCaptainId() {
		return selectedCaptainId;
	}
}
