package com.etermax.spacehorse.core.catalog.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.BotMmrAlgorithmConfiguration;
import com.etermax.spacehorse.core.catalog.exception.NoStartingCardsDefinedInGameConstantsException;
import com.etermax.spacehorse.core.catalog.model.csv.field.Fint;
import com.etermax.spacehorse.core.freechest.model.FreeChestConstants;
import com.etermax.spacehorse.core.matchmaking.model.MatchmakingAlgorithmConfiguration;
import com.etermax.spacehorse.core.player.model.inventory.chest.ChestConstants;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.google.common.collect.Lists;

public class GameConstants implements FreeChestConstants, ChestConstants {

	private int startingEnergy = 12;
	private int maxEnergy = 12;
	private Fint energyPerSecond = Fint.createFromFraction(1, 4);
	private int numberOfCardsInDeck = 8;
	private Fint lastMinuteDuration = Fint.createFromInt(60);
	private Fint lastMinuteEnergyMultiplier = Fint.createFromInt(2);
	private int battleDuration = 180;
	private int suddenDeathDuration = 60;
	private int maxChests = 4;
	private int maxOpeningChests = 1;
	private int openingChestSpeedupCostGems = 1;
	private int openingChestSpeedupCostSeconds = 600;
	private int startingGold = 500;
	private int startingGems = 5;
	private List<String> startingCards = new ArrayList<>();
	private String defaultChestRewardSequenceId = "ListA";
	private String defaultFreeGemsCycleSequenceId = "ListA";
	private String defaultQuestCycleSequenceId = "ListA";

	private String freeChestId = "free";
	private int timeBetweenFreeChestsInSeconds = 8 * 60 * 60;
	private int maxFreeChests = 2;

	private double matchMakingIndexTolerance = 0.25;
	private int matchMakingMmrLimit = 60;
	private int matchMakingTimeoutInSeconds = 10;

	private int mmrAlgorithmDefault = 30;
	private double mmrAlgorithmMultiplier = 10;
	private int cappedMmr = 5000;

	private boolean tutorialAvailable = true;

	private int minimumMmrAfterTutorial = 100;

	private int refreshTimeForEasyQuestSlot = 86400; // 24hs
	private int refreshTimeForMediumQuestSlot = 86400; // 24hs;
	private int refreshTimeForHardQuestSlot = 86400; // 24hs;
	private int skipTimeForQuestBoard = 86400; // 24hs;;
	private int gemsCostToSkip = 10;

	private int cardDropRateCalculatorConfigurationDropDiffBase = 2;
	private int cardDropRateCalculatorConfigurationDropDiffExp = 2;
	private int cardDropRateCalculatorConfigurationDropDiffReducer = 5;
	private int cardDropRateCalculatorConfigurationWinLoseReference = 45;
	private int cardDropRateCalculatorConfigurationWinLoseReducer = 3;

	private int botMmrDeltaOnVictory = 50;
	private int botMmrDeltaOnVictoryPerScoreDiff = 25;

	private int botMmrDeltaOnDefeat = -50;
	private int botMmrDeltaOnDefeatPerScoreDiff = -25;

	private int botMmrDeltaOnTie = -10;
	private boolean useSocialModule = true;

	private boolean activateCaptains = true;

	private boolean useFeaturesByPlayerLvl = true;

	private List<String> startingCaptains = Lists.newArrayList();

	private Fint battleSpeedMultiplier = Fint.createFromInt(1);
	private Fint mainshipSpeedMultiplier = Fint.createFromInt(1);
	private Fint unitsSpeedMultiplier = Fint.createFromInt(1);

	private int rateGameCooldownTime;
	private List<String> rateGameChestIds = Lists.newArrayList();
	private int[] rateGameLevels = new int[] {};
	private int rateGameVictories;
	private int nextQuestRemainingTimeDividerFactor = 720;
	private int nextQuestRemainingTimeGemsCostFactor = 1;
	private String currentDailyQuestId = "daily_quest_definition_1";
	private boolean activateDailyQuest = true;
	private boolean botReactionsEnabled = true;
	private boolean useChestSpeedupVideoReward = true;
	private boolean useBattleVideoReward = false;
	private String videoRewardPlacementsToValidate = "speedup_courier_place,battle_finish_place";
	private boolean useAchievements = false;
	private String leagueMapId = "MapNembu";
	private boolean leagueEnabled = false;

	public List<Tuple> getTuples() {
		ArrayList<Tuple> tuples = new ArrayList<>();
		tuples.add(new Tuple("StartingEnergy", Integer.toString(getStartingEnergy())));
		tuples.add(new Tuple("MaxEnergy", Integer.toString(getMaxEnergy())));
		tuples.add(new Tuple("EnergyPerSecond", Float.toString(getEnergyPerSecond().toFloat())));
		tuples.add(new Tuple("NumberOfCardsInDeck", Integer.toString(getNumberOfCardsInDeck())));
		tuples.add(new Tuple("LastMinuteDuration", Float.toString(getLastMinuteDuration().toFloat())));
		tuples.add(new Tuple("LastMinuteEnergyMultiplier", Float.toString(getLastMinuteEnergyMultiplier().toFloat())));
		tuples.add(new Tuple("BattleDuration", Integer.toString(getBattleDuration())));
		tuples.add(new Tuple("SuddenDeathDuration", Integer.toString(getSuddenDeathDuration())));
		tuples.add(new Tuple("MaxChests", Integer.toString(getMaxChests())));
		tuples.add(new Tuple("MaxOpeningChests", Integer.toString(getMaxOpeningChests())));
		tuples.add(new Tuple("OpeningChestSpeedupCostGems", Integer.toString(getOpeningChestSpeedupCostGems())));
		tuples.add(new Tuple("OpeningChestSpeedupCostSeconds", Integer.toString(getOpeningChestSpeedupCostSeconds())));
		tuples.add(new Tuple("StartingGold", Integer.toString(getStartingGold())));
		tuples.add(new Tuple("StartingGems", Integer.toString(getStartingGems())));
		tuples.add(new Tuple("StartingCards", getStartingCardsConcatenated()));
		tuples.add(new Tuple("DefaultChestRewardSequenceId", getDefaultChestRewardSequenceId()));
		tuples.add(new Tuple("DefaultFreeGemsCycleSequenceId", getDefaultFreeGemsCycleSequenceId()));
		tuples.add(new Tuple("DefaultQuestCycleSequenceId", getDefaultQuestCycleSequenceId()));
		tuples.add(new Tuple("FreeChestId", getFreeChestId()));
		tuples.add(new Tuple("TimeBetweenFreeChestsInSeconds", Integer.toString(getTimeBetweenFreeChestsInSeconds())));
		tuples.add(new Tuple("MaxFreeChests", Integer.toString(getMaxFreeChests())));
		tuples.add(new Tuple("MatchMakingIndexTolerance", Double.toString(getMatchMakingIndexTolerance())));
		tuples.add(new Tuple("MatchMakingMmrLimit", Integer.toString(getMatchMakingMmrLimit())));
		tuples.add(new Tuple("MatchMakingTimeoutInSeconds", Integer.toString(getMatchMakingTimeoutInSeconds())));
		tuples.add(new Tuple("MmrAlgorithmDefault", Integer.toString(getMmrAlgorithmDefault())));
		tuples.add(new Tuple("MmrAlgorithmMultiplier", Double.toString(getMmrAlgorithmMultiplier())));
		tuples.add(new Tuple("TutorialAvailable", Boolean.toString(getTutorialAvailable())));
		tuples.add(new Tuple("MinimumMMR", Integer.toString(getMinimumMMRAfterTutorial())));
		tuples.add(new Tuple("RefreshTimeForEasyQuestSlot", Integer.toString(getRefreshTimeForEasyQuestSlot())));
		tuples.add(new Tuple("RefreshTimeForMediumQuestSlot", Integer.toString(getRefreshTimeForMediumQuestSlot())));
		tuples.add(new Tuple("RefreshTimeForHardQuestSlot", Integer.toString(getRefreshTimeForHardQuestSlot())));
		tuples.add(new Tuple("SkipTimeForQuestBoard", Integer.toString(getSkipTimeForQuestBoard())));
		tuples.add(new Tuple("GemsCostToSkipQuest", Integer.toString(getGemsCostToSkipQuest())));
		tuples.add(new Tuple("NextQuestRemainingTimeDividerFactor", Integer.toString(getNextQuestRemainingTimeDividerFactor())));
		tuples.add(new Tuple("NextQuestRemainingTimeGemsCostFactor", Integer.toString(getNextQuestRemainingTimeGemsCostFactor())));
		tuples.add(new Tuple("CDRCC_DropDiffBase", Integer.toString(getCardDropRateCalculatorConfigurationDropDiffBase())));
		tuples.add(new Tuple("CDRCC_DropDiffExp", Integer.toString(getCardDropRateCalculatorConfigurationDropDiffExp())));
		tuples.add(new Tuple("CDRCC_DropDiffReducer", Integer.toString(getCardDropRateCalculatorConfigurationDropDiffReducer())));
		tuples.add(new Tuple("CDRCC_WinLoseReference", Integer.toString(getCardDropRateCalculatorConfigurationWinLoseReference())));
		tuples.add(new Tuple("CDRCC_WinLoseReducer", Integer.toString(getCardDropRateCalculatorConfigurationWinLoseReducer())));
		tuples.add(new Tuple("BotMmrDeltaOnVictory", Integer.toString(botMmrDeltaOnVictory)));
		tuples.add(new Tuple("BotMmrDeltaOnVictoryPerScoreDiff", Integer.toString(botMmrDeltaOnVictoryPerScoreDiff)));
		tuples.add(new Tuple("BotMmrDeltaOnDefeat", Integer.toString(botMmrDeltaOnDefeat)));
		tuples.add(new Tuple("BotMmrDeltaOnDefeatPerScoreDiff", Integer.toString(botMmrDeltaOnDefeatPerScoreDiff)));
		tuples.add(new Tuple("BotMmrDeltaOnTie", Integer.toString(botMmrDeltaOnTie)));
		tuples.add(new Tuple("UseSocialModule", Boolean.toString(useSocialModule)));
		tuples.add(new Tuple("StartingCaptains", getStartingCaptainsConcatenated()));
		tuples.add(new Tuple("ActivateCaptains", Boolean.toString(activateCaptains)));
		tuples.add(new Tuple("UseFeaturesByPlayerLvl", Boolean.toString(useFeaturesByPlayerLvl)));
		tuples.add(new Tuple("BattleSpeedMultiplier", Float.toString(getBattleSpeedMultiplier().toFloat())));
		tuples.add(new Tuple("MainshipSpeedMultiplier", Float.toString(getMainshipSpeedMultiplier().toFloat())));
		tuples.add(new Tuple("UnitsSpeedMultiplier", Float.toString(getUnitsSpeedMultiplier().toFloat())));
		tuples.add(new Tuple("RateGameCooldownTime", Integer.toString(rateGameCooldownTime)));
		tuples.add(new Tuple("RateGameChestIds", getRateGameChestIdsConcatenated()));
		tuples.add(new Tuple("RateGameLevels", getRateGameLevelsConcatenated()));
		tuples.add(new Tuple("RateGameVictories", Integer.toString(rateGameVictories)));
		tuples.add(new Tuple("CurrentDailyQuestId", getCurrentDailyQuestId()));
		tuples.add(new Tuple("ActivateDailyQuest", Boolean.toString(activateDailyQuest)));
		tuples.add(new Tuple("BotReactionsEnabled", Boolean.toString(botReactionsEnabled)));
		tuples.add(new Tuple("UseChestSpeedupVideoReward", Boolean.toString(useChestSpeedupVideoReward)));
		tuples.add(new Tuple("UseBattleVideoReward", Boolean.toString(useBattleVideoReward)));
		tuples.add(new Tuple("CappedMmr", Integer.toString(cappedMmr)));
		tuples.add(new Tuple("VideoRewardPlacementsToValidate", videoRewardPlacementsToValidate));
		tuples.add(new Tuple("UseAchievements", Boolean.toString(useAchievements)));
		tuples.add(new Tuple("LeagueMapId", leagueMapId));
		tuples.add(new Tuple("LeagueEnabled", Boolean.toString(leagueEnabled)));
		return tuples;
	}

	public List<String> getVideoRewardPlacementsToValidate(){
		String[] videoRewardPlacements = videoRewardPlacementsToValidate.split(",");
		return Lists.newArrayList(videoRewardPlacements);
	}

	public String getLeagueMapId() {
		return leagueMapId;
	}

	private String getRateGameChestIdsConcatenated() {
		return concatenateConstantsAsCsv(rateGameChestIds);
	}

	private String getRateGameLevelsConcatenated() {
		return concatenateConstantsAsCsv(rateGameLevels);
	}

	private String getStartingCaptainsConcatenated() {
		return concatenateConstantsAsCsv(startingCaptains);
	}

	private String getStartingCardsConcatenated() {
		return concatenateConstantsAsCsv(startingCards);
	}

	public int getStartingEnergy() {
		return startingEnergy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public Fint getEnergyPerSecond() {
		return energyPerSecond;
	}

	public int getNumberOfCardsInDeck() {
		return numberOfCardsInDeck;
	}

	public Fint getLastMinuteDuration() {
		return lastMinuteDuration;
	}

	public Fint getLastMinuteEnergyMultiplier() {
		return lastMinuteEnergyMultiplier;
	}

	public int getBattleDuration() {
		return battleDuration;
	}

	public int getSuddenDeathDuration() {
		return suddenDeathDuration;
	}

	public int getMaxChests() {
		return maxChests;
	}

	public int getMaxOpeningChests() {
		return maxOpeningChests;
	}

	public int getOpeningChestSpeedupCostGems() {
		return openingChestSpeedupCostGems;
	}

	public int getOpeningChestSpeedupCostSeconds() {
		return openingChestSpeedupCostSeconds;
	}

	public int getStartingGold() {
		return startingGold;
	}

	public int getStartingGems() {
		return startingGems;
	}

	public String getDefaultChestRewardSequenceId() {
		return defaultChestRewardSequenceId;
	}

	public String getDefaultFreeGemsCycleSequenceId() {
		return defaultFreeGemsCycleSequenceId;
	}

	public String getDefaultQuestCycleSequenceId() {
		return defaultQuestCycleSequenceId;
	}

	public List<String> getStartingCards() {
		return startingCards;
	}

	public String getFreeChestId() {
		return freeChestId;
	}

	public int getTimeBetweenFreeChestsInSeconds() {
		return timeBetweenFreeChestsInSeconds;
	}

	public int getMaxFreeChests() {
		return maxFreeChests;
	}

	public double getMatchMakingIndexTolerance() {
		return matchMakingIndexTolerance;
	}

	public int getMatchMakingMmrLimit() {
		return matchMakingMmrLimit;
	}

	public int getMatchMakingTimeoutInSeconds() {
		return matchMakingTimeoutInSeconds;
	}

	public int getMmrAlgorithmDefault() {
		return mmrAlgorithmDefault;
	}

	public double getMmrAlgorithmMultiplier() {
		return mmrAlgorithmMultiplier;
	}

	public int getMinimumMMRAfterTutorial() {
		return minimumMmrAfterTutorial;
	}

	public boolean getTutorialAvailable() {
		return tutorialAvailable;
	}

	public int getRefreshTimeForEasyQuestSlot() {
		return refreshTimeForEasyQuestSlot;
	}

	public int getRefreshTimeForMediumQuestSlot() {
		return refreshTimeForMediumQuestSlot;
	}

	public int getRefreshTimeForHardQuestSlot() {
		return refreshTimeForHardQuestSlot;
	}

	public int getSkipTimeForQuestBoard() {
		return skipTimeForQuestBoard;
	}

	public int getCardDropRateCalculatorConfigurationDropDiffBase() {
		return cardDropRateCalculatorConfigurationDropDiffBase;
	}

	public int getCardDropRateCalculatorConfigurationDropDiffExp() {
		return cardDropRateCalculatorConfigurationDropDiffExp;
	}

	public int getCardDropRateCalculatorConfigurationDropDiffReducer() {
		return cardDropRateCalculatorConfigurationDropDiffReducer;
	}

	public int getCardDropRateCalculatorConfigurationWinLoseReference() {
		return cardDropRateCalculatorConfigurationWinLoseReference;
	}

	public int getCardDropRateCalculatorConfigurationWinLoseReducer() {
		return cardDropRateCalculatorConfigurationWinLoseReducer;
	}

	public int getBotMmrDeltaOnVictory() {
		return botMmrDeltaOnVictory;
	}

	public int getBotMmrDeltaOnVictoryPerScoreDiff() {
		return botMmrDeltaOnVictoryPerScoreDiff;
	}

	public int getBotMmrDeltaOnDefeat() {
		return botMmrDeltaOnDefeat;
	}

	public int getBotMmrDeltaOnDefeatPerScoreDiff() {
		return botMmrDeltaOnDefeatPerScoreDiff;
	}

	public int getBotMmrDeltaOnTie() {
		return botMmrDeltaOnTie;
	}

	public List<String> getStartingCaptains() {
		return startingCaptains;
	}

	public int getGemsCostToSkipQuest() {
		return gemsCostToSkip;
	}

	public boolean isActivateDailyQuest() {
		return activateDailyQuest;
	}

	public boolean isBotReactionsEnabled() {
		return botReactionsEnabled;
	}

	public boolean isUseAchievements() {
		return useAchievements;
	}


	public CardDropRateCalculatorConfiguration getCardDropRateCalculatorConfiguration() {
		return new CardDropRateCalculatorConfiguration(getCardDropRateCalculatorConfigurationDropDiffBase(),
				getCardDropRateCalculatorConfigurationDropDiffExp(), getCardDropRateCalculatorConfigurationDropDiffReducer(),
				getCardDropRateCalculatorConfigurationWinLoseReference(), getCardDropRateCalculatorConfigurationWinLoseReducer());
	}

	public BotMmrAlgorithmConfiguration getBotMmrAlgorithmConfiguration() {
		return new BotMmrAlgorithmConfiguration(getBotMmrDeltaOnVictory(), getBotMmrDeltaOnVictoryPerScoreDiff(), getBotMmrDeltaOnDefeat(),
				getBotMmrDeltaOnDefeatPerScoreDiff(), getBotMmrDeltaOnTie());
	}

	public MatchmakingAlgorithmConfiguration getMatchmakingAlgorithmConfiguration() {
		return new MatchmakingAlgorithmConfiguration(getMatchMakingIndexTolerance(), getMatchMakingMmrLimit());
	}

	public boolean isUseSocialModule() {
		return useSocialModule;
	}

	public boolean isActivateCaptains() {
		return activateCaptains;
	}

	public boolean isUseFeaturesByPlayerLvl() {
		return useFeaturesByPlayerLvl;
	}

	public Fint getBattleSpeedMultiplier() {
		return battleSpeedMultiplier;
	}

	public Fint getMainshipSpeedMultiplier() {
		return mainshipSpeedMultiplier;
	}

	public Fint getUnitsSpeedMultiplier() {
		return unitsSpeedMultiplier;
	}

	public int getRateGameCooldownTime() {
		return rateGameCooldownTime;
	}

	public List<String> getRateGameChestIds() {
		return rateGameChestIds;
	}

	public int[] getRateGameLevels() {
		return rateGameLevels;
	}

	public int getRateGameVictories() {
		return rateGameVictories;
	}

	public boolean isUseChestSpeedupVideoReward() {
		return useChestSpeedupVideoReward;
	}

	public boolean isUseBattleVideoReward() {
		return useBattleVideoReward;
	}

	public GameConstants(List<Tuple> constants) {
		if (constants == null || constants.isEmpty()) {
			return;
		}
		constants.forEach(constantTuple -> setConstant(constantTuple.getId(), constantTuple.getValue()));
	}

	public int getNextQuestRemainingTimeDividerFactor() {
		return nextQuestRemainingTimeDividerFactor;
	}

	public int getNextQuestRemainingTimeGemsCostFactor() {
		return nextQuestRemainingTimeGemsCostFactor;
	}

	public String getCurrentDailyQuestId() {
		return currentDailyQuestId;
	}

	public int getCappedMmr() {
		return cappedMmr;
	}

	private void setConstant(String key, String value) {
		switch (key) {
			case "StartingEnergy":
				startingEnergy = Integer.parseInt(value);
				break;
			case "MaxEnergy":
				maxEnergy = Integer.parseInt(value);
				break;
			case "EnergyPerSecond":
				energyPerSecond = Fint.createFromString(value);
				break;
			case "NumberOfCardsInDeck":
				numberOfCardsInDeck = Integer.parseInt(value);
				break;
			case "LastMinuteDuration":
				lastMinuteDuration = Fint.createFromString(value);
				break;
			case "LastMinuteEnergyMultiplier":
				lastMinuteEnergyMultiplier = Fint.createFromString(value);
				break;
			case "BattleDuration":
				battleDuration = Integer.parseInt(value);
				break;
			case "SuddenDeathDuration":
				suddenDeathDuration = Integer.parseInt(value);
				break;
			case "MaxChests":
				maxChests = Integer.parseInt(value);
				break;
			case "MaxOpeningChests":
				maxOpeningChests = Integer.parseInt(value);
				break;
			case "OpeningChestSpeedupCostGems":
				openingChestSpeedupCostGems = Integer.parseInt(value);
				break;
			case "OpeningChestSpeedupCostSeconds":
				openingChestSpeedupCostSeconds = Integer.parseInt(value);
				break;
			case "StartingGold":
				startingGold = Integer.parseInt(value);
				break;
			case "StartingGems":
				startingGems = Integer.parseInt(value);
				break;
			case "StartingCards":
				startingCards = Collections.unmodifiableList(splitConstantsString(value));
				break;
			case "DefaultChestRewardSequenceId":
				defaultChestRewardSequenceId = value;
				break;
			case "DefaultFreeGemsCycleSequenceId":
				defaultFreeGemsCycleSequenceId = value;
				break;
			case "DefaultQuestCycleSequenceId":
				defaultQuestCycleSequenceId = value;
				break;
			case "FreeChestId":
				freeChestId = value;
				break;
			case "TimeBetweenFreeChestsInSeconds":
				timeBetweenFreeChestsInSeconds = Integer.parseInt(value);
				break;
			case "MaxFreeChests":
				maxFreeChests = Integer.parseInt(value);
				break;
			case "MatchMakingIndexTolerance":
				matchMakingIndexTolerance = Double.valueOf(value);
				break;
			case "MatchMakingMmrLimit":
				matchMakingMmrLimit = Integer.parseInt(value);
				break;
			case "MatchMakingTimeoutInSeconds":
				matchMakingTimeoutInSeconds = Integer.parseInt(value);
				break;
			case "MmrAlgorithmDefault":
				mmrAlgorithmDefault = Integer.parseInt(value);
				break;
			case "MmrAlgorithmMultiplier":
				mmrAlgorithmMultiplier = Double.valueOf(value);
				break;
			case "TutorialAvailable":
				tutorialAvailable = Boolean.valueOf(value);
				break;
			case "MinimumMMR":
				minimumMmrAfterTutorial = Integer.parseInt(value);
				break;
			case "CappedMmr":
				cappedMmr = Integer.parseInt(value);
				break;
			case "RefreshTimeForEasyQuestSlot":
				refreshTimeForEasyQuestSlot = Integer.parseInt(value);
				break;
			case "RefreshTimeForMediumQuestSlot":
				refreshTimeForMediumQuestSlot = Integer.parseInt(value);
				break;
			case "RefreshTimeForHardQuestSlot":
				refreshTimeForHardQuestSlot = Integer.parseInt(value);
				break;
			case "SkipTimeForQuestBoard":
				skipTimeForQuestBoard = Integer.parseInt(value);
				break;
			case "GemsCostToSkipQuest":
				gemsCostToSkip = Integer.parseInt(value);
				break;
			case "NextQuestRemainingTimeDividerFactor":
				nextQuestRemainingTimeDividerFactor = Integer.parseInt(value);
				break;
			case "NextQuestRemainingTimeGemsCostFactor":
				nextQuestRemainingTimeGemsCostFactor = Integer.parseInt(value);
				break;

			case "CDRCC_DropDiffBase":
				cardDropRateCalculatorConfigurationDropDiffBase = Integer.parseInt(value);
				break;
			case "CDRCC_DropDiffExp":
				cardDropRateCalculatorConfigurationDropDiffExp = Integer.parseInt(value);
				break;
			case "CDRCC_DropDiffReducer":
				cardDropRateCalculatorConfigurationDropDiffReducer = Integer.parseInt(value);
				break;
			case "CDRCC_WinLoseReference":
				cardDropRateCalculatorConfigurationWinLoseReference = Integer.parseInt(value);
				break;
			case "CDRCC_WinLoseReducer":
				cardDropRateCalculatorConfigurationWinLoseReducer = Integer.parseInt(value);
				break;

			case "BotMmrDeltaOnVictory":
				botMmrDeltaOnVictory = Integer.parseInt(value);
				break;
			case "BotMmrDeltaOnVictoryPerScoreDiff":
				botMmrDeltaOnVictoryPerScoreDiff = Integer.parseInt(value);
				break;
			case "BotMmrDeltaOnDefeat":
				botMmrDeltaOnDefeat = Integer.parseInt(value);
				break;
			case "BotMmrDeltaOnDefeatPerScoreDiff":
				botMmrDeltaOnDefeatPerScoreDiff = Integer.parseInt(value);
				break;
			case "BotMmrDeltaOnTie":
				botMmrDeltaOnTie = Integer.parseInt(value);
				break;

			case "UseSocialModule":
				useSocialModule = Boolean.valueOf(value);
				break;
			case "StartingCaptains":
				startingCaptains = Collections.unmodifiableList(splitConstantsString(value));
				break;
			case "ActivateCaptains":
				activateCaptains = Boolean.valueOf(value);
				break;
			case "UseFeaturesByPlayerLvl":
				useFeaturesByPlayerLvl = Boolean.valueOf(value);
				break;

			case "BattleSpeedMultiplier":
				battleSpeedMultiplier = Fint.createFromString(value);
				break;

			case "MainshipSpeedMultiplier":
				mainshipSpeedMultiplier = Fint.createFromString(value);
				break;

			case "UnitsSpeedMultiplier":
				unitsSpeedMultiplier = Fint.createFromString(value);
				break;

			case "RateGameCooldownTime":
				rateGameCooldownTime = Integer.parseInt(value);
				break;
			case "RateGameChestIds":
				rateGameChestIds = Collections.unmodifiableList(splitConstantsString(value));
				break;
			case "RateGameLevels":
				rateGameLevels = splitConstantsIntsString(value);
				break;
			case "RateGameVictories":
				rateGameVictories = Integer.parseInt(value);
				break;
			case "CurrentDailyQuestId":
				currentDailyQuestId = value;
				break;
			case "ActivateDailyQuest":
				activateDailyQuest = Boolean.valueOf(value);
				break;
			case "BotReactionsEnabled":
				botReactionsEnabled = Boolean.valueOf(value);
				break;
			case "UseChestSpeedupVideoReward":
				useChestSpeedupVideoReward = Boolean.valueOf(value);
				break;
			case "UseBattleVideoReward":
				useBattleVideoReward = Boolean.valueOf(value);
				break;
			case "VideoRewardPlacementsToValidate":
				videoRewardPlacementsToValidate = value;
				break;
			case "UseAchievements":
				useAchievements = Boolean.valueOf(value);
				break;
			case "LeagueMapId":
				leagueMapId = value;
				break;
			case "LeagueEnabled":
				leagueEnabled = Boolean.valueOf(value);
				break;
		}
	}

	public boolean isLeagueEnabled() {
		return leagueEnabled;
	}

	private List<String> splitConstantsString(String value) {
		try {
			return Arrays.stream(value.split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
		} catch (Throwable e) {
			throw new NoStartingCardsDefinedInGameConstantsException("Any starting card is defined for game constants");
		}
	}

	private int[] splitConstantsIntsString(String value) {
		String[] intStringSplit = value.split(",");
		int[] result = new int[intStringSplit.length];

		for (int i = 0; i < intStringSplit.length; i++) {
			if (!intStringSplit[i].isEmpty()) {
				result[i] = Integer.parseInt(intStringSplit[i].trim());
			}
		}
		return result;
	}

	private String concatenateConstantsAsCsv(List<String> listOfParameters) {
		StringBuilder parameterBuilder = new StringBuilder();
		listOfParameters.forEach(card -> parameterBuilder.append(card + ","));
		if (parameterBuilder.length() == 0) {
			return "";
		}
		parameterBuilder.deleteCharAt(parameterBuilder.length() - 1);
		return parameterBuilder.toString();
	}

	private String concatenateConstantsAsCsv(int[] listOfParameters) {
		StringBuilder parameterBuilder = new StringBuilder();
		for (int i = 0; i < listOfParameters.length; i++) {
			parameterBuilder.append(listOfParameters[i] + ",");
		}
		if (parameterBuilder.length() == 0) {
			return "";
		}
		parameterBuilder.deleteCharAt(parameterBuilder.length() - 1);
		return parameterBuilder.toString();
	}
}
