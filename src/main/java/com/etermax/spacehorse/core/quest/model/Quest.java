package com.etermax.spacehorse.core.quest.model;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.handler.QuestProgressHandler;
import com.etermax.spacehorse.core.quest.model.handler.QuestProgressHandlerFactory;

@DynamoDBDocument
public class Quest {

	public static final String INITIAL_STATE = "INITIAL";
	public static final String STARTED_STATE = "STARTED";
	public static final String CLAIMED_STATE = "CLAIMED";
	public static final String SKIPPED_STATE = "SKIPPED";

	@DynamoDBAttribute(attributeName = "questId")
	private String questId;

	@DynamoDBAttribute(attributeName = "questType")
	private String questType;

	@DynamoDBAttribute(attributeName = "refreshTimeInSeconds") // Important: Set any value when player claim the first quest.
	private long refreshTimeInSeconds;

	@DynamoDBAttribute(attributeName = "goalAmount")
	private long goalAmount;

	@DynamoDBAttribute(attributeName = "state")
	private String state;

	@DynamoDBAttribute(attributeName = "currentProgress")
	private long currentProgress;

	public Quest() {
		this.questId = "";
		this.questType = "";
		this.state = INITIAL_STATE;
	}

	public Quest(QuestDefinition questDefinition) {
		this.questId = questDefinition.getId();
		this.questType = questDefinition.getQuestType();
		this.currentProgress = 0;
		this.goalAmount = questDefinition.getGoalAmount();
		this.refreshTimeInSeconds = 0;
		this.state = INITIAL_STATE;
	}

	public Quest(QuestDefinition questDefinition, long refreshTimeInSeconds) {
		this(questDefinition);
		this.refreshTimeInSeconds = refreshTimeInSeconds;
		this.state = STARTED_STATE;
	}

	@DynamoDBIgnore
	public boolean isCompleted() {
		return getRemainingAmount() == 0;
	}

	@DynamoDBIgnore
	public long getRemainingAmount() {
		return clamp(goalAmount - currentProgress, 0, goalAmount);
	}

	@DynamoDBIgnore
	public boolean canSetRemainingAmount(long remainingAmount) {
		return remainingAmount >= 0 && remainingAmount <= goalAmount;
	}

	@DynamoDBIgnore
	public void cheatSetRemainingAmount(long remainingAmount) {

		if (!canSetRemainingAmount(remainingAmount)) {
			throw new IllegalArgumentException("Invalid remaining amount value " + remainingAmount);
		}

		this.currentProgress = goalAmount - remainingAmount;
	}

	@DynamoDBIgnore
	public boolean refreshTimeIsNotExpired(long serverTime) {
		return getRemainingTime(serverTime) > 0;
	}

	@DynamoDBIgnore
	private long getRemainingTime(long serverTime) {
		if (serverTime < refreshTimeInSeconds) {
			return refreshTimeInSeconds - serverTime;
		}
		return 0;
	}

	@DynamoDBIgnore
	public boolean isStarted() {
		return this.state.equalsIgnoreCase(STARTED_STATE);
	}

	@DynamoDBIgnore
	public boolean isClaimed() {
		return this.state.equalsIgnoreCase(CLAIMED_STATE);
	}

	@DynamoDBIgnore
	public boolean isSkipped() {
		return this.state.equalsIgnoreCase(SKIPPED_STATE);
	}

	@DynamoDBIgnore
	public boolean isInitial() {
		return this.state.equalsIgnoreCase(INITIAL_STATE);
	}

	@DynamoDBIgnore
	private Optional<QuestProgressHandler> getQuestProgressHandler(QuestProgressHandlerFactory factory) {
		if (!StringUtils.isEmpty(questType))
			return Optional.of(factory.build(questType));
		return Optional.empty();
	}

	@DynamoDBIgnore
	public void start() {
		this.state = STARTED_STATE;
	}

	@DynamoDBIgnore
	public void claim() {
		this.state = CLAIMED_STATE;
	}

	@DynamoDBIgnore
	public void skip() {
		this.state = SKIPPED_STATE;
	}

	@DynamoDBIgnore
	public void onQuestDefinitionChanged(CatalogEntriesCollection<QuestDefinition> questDefinitions) {
		if (isNotBlank(questId)) {
			questDefinitions.findById(questId).ifPresent(questDefinition -> {
				goalAmount = questDefinition.getGoalAmount();
			});
		}
	}

	static private long clamp(long value, long min, long max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	@DynamoDBIgnore
	public void incrementProgress() {
		if (currentProgress < goalAmount)
			currentProgress++;
	}

	@DynamoDBIgnore
	public boolean handleBattleEnded(Battle battle, Player player, QuestProgressHandlerFactory questProgressHandlerFactory, long currentTimeInSeconds) {

		if (!isValid(currentTimeInSeconds)) {
			return false;
		}
		return getQuestProgressHandler(questProgressHandlerFactory).map(progressHandler -> progressHandler.handleBattleEnded(this, battle, player))
				.orElse(false);
	}

	@DynamoDBIgnore
	private boolean isValid(long currentTimeInSeconds) {
		return !StringUtils.isEmpty(questId) && !StringUtils.isEmpty(questType) || refreshTimeIsValid(currentTimeInSeconds);
	}

	private boolean refreshTimeIsValid(long currentTimeInSeconds) {
		if (refreshTimeInSeconds != 0) {
			return !refreshTimeIsNotExpired(currentTimeInSeconds);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	/**
	 * getters & setter for dynamo mapper
	 **/

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public String getQuestType() {
		return questType;
	}

	public void setQuestType(String questType) {
		this.questType = questType;
	}

	public long getRefreshTimeInSeconds() {
		return refreshTimeInSeconds;
	}

	public void setRefreshTimeInSeconds(long refreshTimeInSeconds) {
		this.refreshTimeInSeconds = refreshTimeInSeconds;
	}

	public long getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(long goalAmount) {
		this.goalAmount = goalAmount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(long currentProgress) {
		this.currentProgress = currentProgress;
	}

}
