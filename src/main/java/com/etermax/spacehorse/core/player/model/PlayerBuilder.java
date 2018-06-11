package com.etermax.spacehorse.core.player.model;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.progress.PlayerRewards;
import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.shop.model.DynamicShop;

public class PlayerBuilder {
	private String userId = "";
	private String catalogId = "";
	private String name = "";
	private Gender gender;
	private int age;
	private int mapNumber = 0;
	private int maxMapNumber = 0;
	private Deck deck = new Deck();
	private Inventory inventory = new Inventory();
	private PlayerProgress progress = new PlayerProgress();
	private PlayerRewards playerRewards = new PlayerRewards(new ServerTime().getTimeNowAsSeconds());
	private DynamicShop dynamicShop = new DynamicShop();
	private FreeChest freeChest = new FreeChest();
	private TutorialProgress tutorialProgress = new TutorialProgress();
	private PlayerStats playerStats = new PlayerStats();
	private String lastBattleId = "";
	private ABTag abTag = ABTag.emptyABTag();
	private int botMmr = 0;

	public PlayerBuilder setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public PlayerBuilder setCatalogId(String catalogId) {
		this.catalogId = catalogId;
		return this;
	}

	public PlayerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public PlayerBuilder setGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public PlayerBuilder setAge(int age) {
		this.age = age;
		return this;
	}

	public PlayerBuilder setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
		return this;
	}

	public PlayerBuilder setMaxMapNumber(int maxMapNumber) {
		this.maxMapNumber = maxMapNumber;
		return this;
	}

	public PlayerBuilder setDeck(Deck deck) {
		this.deck = deck;
		return this;
	}

	public PlayerBuilder setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}

	public PlayerBuilder setProgress(PlayerProgress progress) {
		this.progress = progress;
		return this;
	}

	public PlayerBuilder setPlayerRewards(PlayerRewards playerRewards) {
		this.playerRewards = playerRewards;
		return this;
	}

	public PlayerBuilder setDynamicShop(DynamicShop dynamicShop) {
		this.dynamicShop = dynamicShop;
		return this;
	}

	public PlayerBuilder setFreeChest(FreeChest freeChest) {
		this.freeChest = freeChest;
		return this;
	}

	public PlayerBuilder setTutorialProgress(TutorialProgress tutorialProgress) {
		this.tutorialProgress = tutorialProgress;
		return this;
	}

	public PlayerBuilder setPlayerStats(PlayerStats playerStats) {
		this.playerStats = playerStats;
		return this;
	}

	public PlayerBuilder setLastBattleId(String lastBattleId) {
		this.lastBattleId = lastBattleId;
		return this;
	}

	public PlayerBuilder setAbTag(ABTag abTag) {
		this.abTag = abTag;
		return this;
	}

	public PlayerBuilder setBotMmr(int botMmr) {
		this.botMmr = botMmr;
		return this;
	}

	public Player createPlayer() {
		return new Player(userId, catalogId, name, gender, age, mapNumber, maxMapNumber, deck, inventory, progress, playerRewards, dynamicShop, freeChest, tutorialProgress,
				playerStats, lastBattleId, abTag, botMmr);
	}
}