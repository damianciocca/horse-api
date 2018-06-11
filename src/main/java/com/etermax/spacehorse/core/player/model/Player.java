package com.etermax.spacehorse.core.player.model;

import java.util.Date;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.progress.PlayerRewards;
import com.etermax.spacehorse.core.player.model.progress.RewardsReceivedToday;
import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.shop.model.DynamicShop;

public class Player {

	public static final String DEFAULT_PLAYER_NAME = "DEFAULT_PLAYER_NAME";
	public static final int DEFAULT_MAP_NUMBER = 0;
	public static final int DEFAULT_BOT_MMR = 0;
	public static final int NOT_INITIALIZED_BOT_MMR = -1;
	public static final Gender DEFAULT_GENDER = Gender.UNKNOWN;
	public static final int DEFAULT_AGE = 0;

	private String userId;
	private String catalogId;
	private ABTag abTag;
	private String name;
	private Gender gender;
	private int age;
	private int mapNumber;
	private int maxMapNumber;
	private Deck deck;
	private Inventory inventory;
	private PlayerProgress progress;
	private PlayerRewards playerRewards;
	private DynamicShop dynamicShop;
	private FreeChest freeChest;
	private TutorialProgress tutorialProgress;
	private PlayerStats playerStats;
	private String lastBattleId;
	private Date createdDate;
	private Date lastUpdatedDate;
	private int botMmr;

	public Player(String userId, String catalogId, String name, Gender gender, int age, int mapNumber, int maxMapNumber, Deck deck, Inventory inventory, PlayerProgress progress,
			PlayerRewards playerRewards, DynamicShop dynamicShop, FreeChest freeChest, TutorialProgress tutorialProgress, PlayerStats playerStats,
			String lastBattleId, ABTag abTag, int botMmr) {
		this.userId = userId;
		this.catalogId = catalogId;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.mapNumber = mapNumber;
		this.maxMapNumber = maxMapNumber;
		this.deck = deck;
		this.inventory = inventory;
		this.progress = progress;
		this.playerRewards = playerRewards;
		this.dynamicShop = dynamicShop;
		this.freeChest = freeChest;
		this.tutorialProgress = tutorialProgress;
		this.playerStats = playerStats;
		this.lastBattleId = lastBattleId;
		this.abTag = abTag;
		this.botMmr = botMmr;
	}

	public static Player buildNewPlayer(String id, ABTag abTag, long timeNowInSeconds, NewPlayerConfiguration configuration) {

		Deck deck = Deck.buildNewPlayerDeck(configuration.getDeckConfiguration());
		Inventory inventory = Inventory.createInventory(configuration.getInventoryConfiguration());
		FreeChest freeChest = FreeChest.buildNewFreeChest(ServerTime.toDate(timeNowInSeconds), configuration.getFreeChestConfiguration());

		TutorialProgress tutorialProgress = TutorialProgress.buildNewTutorialProgress();
		PlayerStats playerStats = new PlayerStats();
		return new PlayerBuilder().setUserId(id).setCatalogId(configuration.getCatalogId()).setName(DEFAULT_PLAYER_NAME).setGender(DEFAULT_GENDER)
				.setAge(DEFAULT_AGE).setMapNumber(DEFAULT_MAP_NUMBER).setDeck(deck).setInventory(inventory).setProgress(new PlayerProgress())
				.setPlayerRewards(new PlayerRewards(timeNowInSeconds)).setDynamicShop(new DynamicShop()).setFreeChest(freeChest)
				.setTutorialProgress(tutorialProgress).setPlayerStats(playerStats).setAbTag(abTag).setBotMmr(DEFAULT_BOT_MMR).createPlayer();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
		this.maxMapNumber = Math.max(mapNumber, getMaxMapNumber());
	}

	public int getMaxMapNumber() {
		return maxMapNumber;
	}

	public Deck getDeck() {
		return deck;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public PlayerProgress getProgress() {
		return progress;
	}

	public PlayerRewards getPlayerRewards() {
		return playerRewards;
	}

	public DynamicShop getDynamicShop() {
		return dynamicShop;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public void checkAndFixIntegrity(Catalog catalog, long timeNowInSeconds) {
		inventory.checkIntegrity(catalog);
		deck.checkAndFixIntegrity(catalog);
		playerRewards.checkAndFixIntegrity(catalog, timeNowInSeconds);
		dynamicShop.checkIntegrity(catalog);
		if (gender == null) {
			gender = Gender.UNKNOWN;
		}

		Date timeNow = ServerTime.toDate(timeNowInSeconds);
		if (freeChest == null) {
			freeChest = FreeChest.buildNewFreeChest(timeNow, catalog.getGameConstants());
		}
		if (playerStats == null) {
			playerStats = new PlayerStats();
		}
		playerStats.checkAndFixIntegrity(catalog);
		if (abTag == null) {
			abTag = ABTag.emptyABTag();
		}
		if (createdDate == null) {
			createdDate = timeNow;
		}
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public FreeChest getFreeChest() {
		return freeChest;
	}

	public void validateTutorialProgress(String tutorialId) {
		tutorialProgress.validateTutorialId(tutorialId);
	}

	public Boolean isTutorialIdActive(String tutorialId) {
		return tutorialProgress.isTutorialIdActive(tutorialId);
	}

	public void setActiveTutorial(String tutorialId) {
		tutorialProgress.setActiveTutorial(tutorialId);
	}

	public boolean hasActiveTutorial() {
		return tutorialProgress.hasActiveTutorial();
	}

	public void finishActiveTutorial() {
		tutorialProgress.finishActiveTutorial();
	}

	public TutorialProgress getTutorialProgress() {
		return tutorialProgress;
	}

	public String getActiveTutorial() {
		return tutorialProgress.getActiveTutorial();
	}

	public void addFinishedTutorial(String tutorialId) {
		tutorialProgress.addFinishedTutorial(tutorialId);
	}

	public boolean isTutorialIdFinished(String tutorialId) {
		return tutorialProgress.isTutorialIdFinished(tutorialId);
	}

	public void removeFinishedTutorialId(String tutorialId) {
		tutorialProgress.removeFinishedTutorialId(tutorialId);
	}

	public String getLastBattleId() {
		return lastBattleId;
	}

	public void setLastBattleId(String lastBattleId) {
		this.lastBattleId = lastBattleId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public RewardsReceivedToday getRewardsReceivedToday() {
		return getPlayerRewards().getRewardsReceivedToday();
	}

	public ABTag getAbTag() {
		return abTag;
	}

	public int getBotMmr() {
		return botMmr;
	}

	public void setBotMmr(int botMmr) {
		this.botMmr = botMmr;
	}

	public void cheatSetAbTag(ABTag abTag) {
		this.abTag = abTag;
	}
}
