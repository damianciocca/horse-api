package com.etermax.spacehorse.core.player.resource.response.player;

import java.util.Optional;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.resource.AchievementCollectionResponse;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.resource.CaptainsCollectionResponse;
import com.etermax.spacehorse.core.freechest.resource.response.FreeChestResponse;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.response.PlayerLeagueResponse;
import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.deck.DeckResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.InventoryResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.PlayerProgressResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.PlayerRewardsResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.TutorialProgressResponse;
import com.etermax.spacehorse.core.player.resource.response.player.questboard.QuestBoardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.stats.PlayerStatsResponse;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.shop.resource.response.DynamicShopResponse;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.resource.response.SpecialOfferBoardResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerResponse {
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("gender")
	private Gender gender;

	@JsonProperty("age")
	private int age;

	@JsonProperty("mapNumber")
	private int mapNumber;

	@JsonProperty("maxMapNumber")
	private int maxMapNumber;

	@JsonProperty("deck")
	private DeckResponse deck;

	@JsonProperty("inventory")
	private InventoryResponse inventory;

	@JsonProperty("progress")
	private PlayerProgressResponse progress;

	@JsonProperty("playerRewards")
	private PlayerRewardsResponse playerRewards;

	@JsonProperty("dynamicShop")
	private DynamicShopResponse dynamicShop;

	@JsonProperty("freeChest")
	private FreeChestResponse freeChestResponse;

	@JsonProperty("mmr")
	private Integer mmr;

	@JsonProperty("botMmr")
	private Integer botMmr;

	@JsonProperty("tutorialProgress")
	private TutorialProgressResponse tutorialProgressResponse;

	@JsonProperty("playerStats")
	private PlayerStatsResponse playerStatsResponse;

	@JsonProperty("abTag")
	private String abTab;

	@JsonProperty("questBoard")
	private QuestBoardResponse questBoardResponse;

	@JsonProperty("specialOfferBoard")
	private SpecialOfferBoardResponse specialOfferBoardResponse;

	@JsonProperty("captainsCollection")
	private CaptainsCollectionResponse captainsCollectionResponse;

	@JsonProperty("createdDateInSeconds")
	private long createdDateInSeconds;

	@JsonProperty("achievementsCollection")
	private AchievementCollectionResponse achievementCollectionResponse;

	@JsonProperty("playerLeague")
	private PlayerLeagueResponse playerLeagueResponse;

	// Fecha informativa de expiracion de la liga (para mapa championsMap)
	@JsonProperty("leagueExpirationServerTime")
	private long leagueExpirationInSeconds;

	@JsonIgnore
	private SpecialOfferBoard specialOfferBoard;

	public PlayerResponse(Player player, Integer mmr, QuestBoard questBoard, SpecialOfferBoard specialOfferBoard,
			CaptainsCollection captainsCollection, AchievementCollection achievementCollection, Optional<PlayerLeague> playerSeasonsOptional,
			long leagueExpirationInSeconds) {
		if (player != null) {
			this.userId = player.getUserId();
			this.name = player.getName();
			this.gender = player.getGender();
			this.age = player.getAge();
			this.mapNumber = player.getMapNumber();
			this.maxMapNumber = player.getMaxMapNumber();
			this.botMmr = player.getBotMmr();
			this.deck = new DeckResponse(player.getDeck());
			this.inventory = new InventoryResponse(player.getInventory());
			this.progress = new PlayerProgressResponse(player.getProgress());
			this.dynamicShop = new DynamicShopResponse(player.getDynamicShop());
			this.freeChestResponse = new FreeChestResponse(player.getFreeChest());
			this.tutorialProgressResponse = new TutorialProgressResponse(player.getTutorialProgress());
			this.playerStatsResponse = new PlayerStatsResponse(player.getPlayerStats());
			this.playerRewards = new PlayerRewardsResponse(player.getPlayerRewards());
			this.abTab = player.getAbTag().toString();
			this.questBoardResponse = new QuestBoardResponse(questBoard);
			this.specialOfferBoardResponse = new SpecialOfferBoardResponse(specialOfferBoard);
			this.specialOfferBoard = specialOfferBoard;
			this.captainsCollectionResponse = new CaptainsCollectionResponse(captainsCollection);
			this.createdDateInSeconds = ServerTime.fromDate(player.getCreatedDate());
			this.achievementCollectionResponse = new AchievementCollectionResponse(achievementCollection);
			playerSeasonsOptional.ifPresent(playerSeasons -> playerLeagueResponse = new PlayerLeagueResponse(playerSeasons));
			this.leagueExpirationInSeconds = leagueExpirationInSeconds;
		}
		this.mmr = mmr;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public DeckResponse getDeck() {
		return deck;
	}

	public InventoryResponse getInventory() {
		return inventory;
	}

	public PlayerProgressResponse getProgress() {
		return progress;
	}

	public DynamicShopResponse getDynamicShop() {
		return dynamicShop;
	}

	public FreeChestResponse getFreeChestResponse() {
		return freeChestResponse;
	}

	public TutorialProgressResponse getTutorialProgressResponse() {
		return tutorialProgressResponse;
	}

	public PlayerStatsResponse getPlayerStatsResponse() {
		return playerStatsResponse;
	}

	public PlayerRewardsResponse getPlayerRewards() {
		return playerRewards;
	}

	public Integer getMmr() {
		return mmr;
	}

	public Integer getBotMmr() {
		return botMmr;
	}

	public String getAbTab() {
		return abTab;
	}

	public QuestBoardResponse getQuestBoardResponse() {
		return questBoardResponse;
	}

	public SpecialOfferBoardResponse getSpecialOfferBoardResponse() {
		return specialOfferBoardResponse;
	}

	public long getCreatedDateInSeconds() {
		return createdDateInSeconds;
	}

	public SpecialOfferBoard getSpecialOfferBoard() {
		return specialOfferBoard;
	}

	public PlayerLeagueResponse getPlayerLeagueResponse() {
		return playerLeagueResponse;
	}

	public long getLeagueExpirationInSeconds() {
		return leagueExpirationInSeconds;
	}
}
