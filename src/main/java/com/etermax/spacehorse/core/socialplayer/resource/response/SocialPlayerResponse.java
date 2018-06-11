package com.etermax.spacehorse.core.socialplayer.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.deck.DeckResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.PlayerProgressResponse;
import com.etermax.spacehorse.core.player.resource.response.player.stats.PlayerStatsResponse;
import com.etermax.spacehorse.core.socialplayer.model.SocialPlayer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SocialPlayerResponse {

	@JsonProperty("userId")
	public String userId;

	@JsonProperty("name")
	public String name;

	@JsonProperty("mmr")
	public int mmr;

	@JsonProperty("deck")
	public DeckResponse deck;

	@JsonProperty("progress")
	public PlayerProgressResponse progress;

	@JsonProperty("playerStats")
	public PlayerStatsResponse playerStats;

	@JsonProperty("mapNumber")
	private int mapNumber;

	@JsonProperty("selectedCaptainId")
	public String selectedCaptainId;

	public SocialPlayerResponse(SocialPlayer socialPlayer) {
		this.userId = socialPlayer.getUserId();
		this.name = socialPlayer.getName();
		this.mmr = socialPlayer.getMmr();
		this.mapNumber = socialPlayer.getMapNumber();
		this.deck = new DeckResponse(socialPlayer.getDeck());
		this.progress = new PlayerProgressResponse(socialPlayer.getProgress());
		this.playerStats = new PlayerStatsResponse(socialPlayer.getPlayerStats());
		this.selectedCaptainId = socialPlayer.getSelectedCaptainId();
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public DeckResponse getDeck() {
		return deck;
	}

	public PlayerProgressResponse getProgress() {
		return progress;
	}

	public PlayerStatsResponse getPlayerStats() {
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
