package com.etermax.spacehorse.core.quest.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

@DynamoDBDocument
public class QuestSlot {

	@DynamoDBAttribute(attributeName = "sequence")
	private int sequence;

	@DynamoDBAttribute(attributeName = "activeQuest")
	private Quest activeQuest;

	public QuestSlot() {
		this.sequence = 0;
		this.activeQuest = new Quest();
	}

	@DynamoDBIgnore
	public boolean hasStartedQuest() {
		return getActiveQuest().isStarted();
	}

	@DynamoDBIgnore
	public boolean hasClaimedQuest() {
		return getActiveQuest().isClaimed();
	}

	@DynamoDBIgnore
	public boolean hasCompletedQuest() {
		return getActiveQuest().isCompleted();
	}

	@DynamoDBIgnore
	public boolean hasSkippedQuest() {
		return getActiveQuest().isSkipped();
	}

	@DynamoDBIgnore
	public boolean hasInitialQuest() {
		return getActiveQuest().isInitial();
	}

	@DynamoDBIgnore
	public void claim(long refreshTimeInSeconds) {
		getActiveQuest().claim();
		setRefreshTimeInSeconds(refreshTimeInSeconds);
	}

	@DynamoDBIgnore
	public long getRefreshTimeInSeconds() {
		return getActiveQuest().getRefreshTimeInSeconds();
	}

	@DynamoDBIgnore
	public void addQuest(Quest quest) {
		this.activeQuest = quest;
		this.sequence++;
		this.activeQuest.start();
	}

	@DynamoDBIgnore
	public void skip() {
		getActiveQuest().skip();
	}

	@DynamoDBIgnore
	public boolean refreshTimeIsNotReached(long timeNowAsSeconds) {
		return getActiveQuest().refreshTimeIsNotExpired(timeNowAsSeconds);
	}

	/**
	 * getters & setter for dynamo mapper
	 **/

	public Quest getActiveQuest() {
		return activeQuest;
	}

	public void setActiveQuest(Quest activeQuest) {
		this.activeQuest = activeQuest;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	private void setRefreshTimeInSeconds(long refreshTimeInSeconds) {
		getActiveQuest().setRefreshTimeInSeconds(refreshTimeInSeconds);
	}
}
