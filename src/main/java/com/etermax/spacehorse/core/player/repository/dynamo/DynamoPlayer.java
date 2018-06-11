package com.etermax.spacehorse.core.player.repository.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.progress.PlayerRewards;
import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.shop.model.DynamicShop;

@DynamoDBTable(tableName = "player")
public class DynamoPlayer implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "catalogId")
	private String catalogId;

	@DynamoDBAttribute(attributeName = "abTag")
	private String abTag;

	@DynamoDBAttribute(attributeName = "name")
	private String name;

	@DynamoDBAttribute(attributeName = "mapNumber")
	private int mapNumber;

	@DynamoDBAttribute(attributeName = "maxMapNumber")
	private int maxMapNumber;

	@DynamoDBAttribute(attributeName = "deck")
	private Deck deck;

	@DynamoDBAttribute(attributeName = "inventory")
	private Inventory inventory;

	@DynamoDBAttribute(attributeName = "progress")
	private PlayerProgress progress;

	@DynamoDBAttribute(attributeName = "rewards")
	private PlayerRewards rewards;

	@DynamoDBAttribute(attributeName = "dynamicShop")
	private DynamicShop dynamicShop;

	@DynamoDBAttribute(attributeName = "freeChest")
	private FreeChest freeChest;

	@DynamoDBAttribute(attributeName = "tutorialProgress")
	private TutorialProgress tutorialProgress;

	@DynamoDBAttribute(attributeName = "playerStats")
	private PlayerStats playerStats;

	@DynamoDBAttribute(attributeName = "lastBattleId")
	private String lastBattleId;

	@DynamoDBAttribute(attributeName = "botMmr")
	private int botMmr = Player.NOT_INITIALIZED_BOT_MMR;

	@DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
	private Date createdDate;

	@DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
	private Date lastUpdatedDate;

	@DynamoDBAttribute(attributeName = "playerAge")
	private int playerAge;

	@DynamoDBAttribute(attributeName = "playerGender")
	private String playerGender = Gender.UNKNOWN.toString();

	public DynamoPlayer(String userId, String catalogId, String name, Gender gender, int age, int mapNumber, int maxMapNumber, Deck deck, Inventory inventory,
			PlayerProgress progress, PlayerRewards rewards, DynamicShop dynamicShop, FreeChest freeChest, TutorialProgress tutorialProgress,
			PlayerStats playerStats, String lastBattleId, ABTag abTag, int botMmr) {
		this.userId = userId;
		this.catalogId = catalogId;
		this.name = name;
		this.mapNumber = mapNumber;
		this.maxMapNumber = maxMapNumber;
		this.deck = deck;
		this.inventory = inventory;
		this.progress = progress;
		this.rewards = rewards;
		this.dynamicShop = dynamicShop;
		this.freeChest = freeChest;
		this.tutorialProgress = tutorialProgress;
		this.playerStats = playerStats;
		this.lastBattleId = lastBattleId;
		this.abTag = abTag.toString();
		this.botMmr = botMmr;
		this.playerAge = age;
		this.playerGender = gender.toString();
	}

	public DynamoPlayer(long timeNowInSeconds) {
		this.deck = new Deck();
		this.inventory = new Inventory();
		this.progress = new PlayerProgress();
		this.rewards = new PlayerRewards(timeNowInSeconds);
		this.dynamicShop = new DynamicShop();
		this.freeChest = new FreeChest();
		this.tutorialProgress = new TutorialProgress();
		this.playerStats = new PlayerStats();
		this.abTag = "";
		this.playerGender = Gender.UNKNOWN.toString();
	}

	public DynamoPlayer() {
		// just for dynamo mapper
	}

	public static DynamoPlayer createFromPlayer(Player player) {
		String userId = player.getUserId();
		String catalogId = player.getCatalogId();
		String name = player.getName();
		Gender gender = player.getGender();
		int age = player.getAge();
		int mapNumber = player.getMapNumber();
		int maxMapNumber = player.getMaxMapNumber();
		Deck deck = player.getDeck();
		PlayerProgress progress = player.getProgress();
		Inventory inventory = player.getInventory();
		PlayerRewards rewards = player.getPlayerRewards();
		DynamicShop dynamicShop = player.getDynamicShop();
		FreeChest freeChest = player.getFreeChest();
		TutorialProgress tutorialProgress = player.getTutorialProgress();
		PlayerStats playerStats = player.getPlayerStats();
		String lastBattleId = player.getLastBattleId();
		ABTag abTag = player.getAbTag();
		int botMmr = player.getBotMmr();
		DynamoPlayer dynamoPlayer = new DynamoPlayer(userId, catalogId, name, gender, age, mapNumber, maxMapNumber, deck, inventory, progress, rewards,
				dynamicShop, freeChest, tutorialProgress, playerStats, lastBattleId, abTag, botMmr);
		if (player.getCreatedDate() != null) {
			dynamoPlayer.setCreatedDate(player.getCreatedDate());
		}
		return dynamoPlayer;
	}

	public static Player mapFromDynamoPlayerToPlayer(DynamoPlayer dynamoPlayer) {
		String userId = dynamoPlayer.getUserId();
		String catalogId = dynamoPlayer.getCatalogId();
		String name = dynamoPlayer.getName();
		Gender gender = Gender.valueOf(dynamoPlayer.getPlayerGender());
		int age = dynamoPlayer.getPlayerAge();

		int mapNumber = dynamoPlayer.getMapNumber();
		int maxMapNumber = Math.max(dynamoPlayer.getMaxMapNumber(), mapNumber);
		Deck deck = dynamoPlayer.getDeck();
		Inventory inventory = dynamoPlayer.getInventory();
		PlayerProgress progress = dynamoPlayer.getProgress();
		PlayerRewards rewards = dynamoPlayer.getRewards();
		DynamicShop dynamicShop = dynamoPlayer.getDynamicShop();
		FreeChest freeChest = dynamoPlayer.getFreeChest();
		TutorialProgress tutorialProgress = dynamoPlayer.getTutorialProgress();
		PlayerStats playerStats = dynamoPlayer.getPlayerStats();
		String lastBattleId = dynamoPlayer.getLastBattleId();
		ABTag abTag = new ABTag(dynamoPlayer.getAbTag());
		int botMmr = dynamoPlayer.getBotMmr();
		Player player = new PlayerBuilder().setUserId(userId).setCatalogId(catalogId).setName(name).setGender(gender).setAge(age).setMapNumber(mapNumber).setMaxMapNumber
				(maxMapNumber).setDeck(deck).setInventory(inventory).setProgress(progress).setPlayerRewards(rewards).setDynamicShop(dynamicShop)
				.setFreeChest(freeChest).setTutorialProgress(tutorialProgress).setPlayerStats(playerStats).setLastBattleId(lastBattleId)
				.setAbTag(abTag).setBotMmr(botMmr).createPlayer();
		player.setCreatedDate(dynamoPlayer.getCreatedDate());
		player.setLastUpdatedDate(dynamoPlayer.getLastUpdatedDate());
		return player;
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

	public int getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
	}

	public int getMaxMapNumber() {
		return maxMapNumber;
	}

	public void setMaxMapNumber(int maxMapNumber) {
		this.maxMapNumber = maxMapNumber;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public PlayerProgress getProgress() {
		return progress;
	}

	public void setProgress(PlayerProgress progress) {
		this.progress = progress;
	}

	public PlayerRewards getRewards() {
		return rewards;
	}

	public void setRewards(PlayerRewards rewards) {
		this.rewards = rewards;
	}

	public DynamicShop getDynamicShop() {
		return dynamicShop;
	}

	public void setDynamicShop(DynamicShop dynamicShop) {
		this.dynamicShop = dynamicShop;
	}

	public FreeChest getFreeChest() {
		return freeChest;
	}

	public void setFreeChest(FreeChest freeChest) {
		this.freeChest = freeChest;
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}

	@Override
	public String getId() {
		return getUserId();
	}

	public TutorialProgress getTutorialProgress() {
		return tutorialProgress;
	}

	public void setTutorialProgress(TutorialProgress tutorialProgress) {
		this.tutorialProgress = tutorialProgress;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public void setPlayerStats(PlayerStats playerStats) {
		this.playerStats = playerStats;
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

	public void setAbTag(String abTag) {
		this.abTag = abTag;
	}

	public String getAbTag() {
		return this.abTag;
	}

	public int getBotMmr() {
		return botMmr;
	}

	public void setBotMmr(int botMmr) {
		this.botMmr = botMmr;
	}

	public int getPlayerAge() {
		return playerAge;
	}

	public void setPlayerAge(int playerAge) {
		this.playerAge = playerAge;
	}

	public String getPlayerGender() {
		return playerGender;
	}

	public void setPlayerGender(String playerGender) {
		this.playerGender = playerGender;
	}
}
