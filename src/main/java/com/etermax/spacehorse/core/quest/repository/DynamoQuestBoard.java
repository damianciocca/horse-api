package com.etermax.spacehorse.core.quest.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.Maps;

@DynamoDBTable(tableName = "questBoard")
public class DynamoQuestBoard implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "slots")
	private List<DynamoSlot> slots;

	@DynamoDBAttribute(attributeName = "skipTimeInSeconds")
	private long skipTimeInSeconds;

	@DynamoDBAttribute(attributeName = "dailyQuest")
	private Quest dailyQuest;

	public DynamoQuestBoard() {
		// just for dynamo mapper
	}

	public DynamoQuestBoard(String userId, QuestBoard questBoard) {
		this.userId = userId;
		slots = createDynamoSlotsFrom(questBoard);
		skipTimeInSeconds = questBoard.getSkipTimeInSeconds();
		dailyQuest = questBoard.getDailyQuest();
	}

	public QuestBoard toQuestBoard(ServerTimeProvider timeProvider, QuestBoardConfiguration configuration) {
		Map<String, QuestSlot> questSlotsMap = Maps.newConcurrentMap();
		slots.forEach(dynamoSlot -> questSlotsMap.put(dynamoSlot.getSlotId(), dynamoSlot.getQuestSlot()));
		if(dailyQuest == null){
			dailyQuest = new Quest();
		}
		return QuestBoard.restore(timeProvider, questSlotsMap, dailyQuest, skipTimeInSeconds, configuration);
	}

	private static List<DynamoSlot> createDynamoSlotsFrom(QuestBoard questBoard) {
		Map<String, QuestSlot> slots = questBoard.getSlots();
		return slots.entrySet().stream().map(toDynamoSlot()).collect(Collectors.toList());
	}

	private static Function<Map.Entry<String, QuestSlot>, DynamoSlot> toDynamoSlot() {
		return slot -> new DynamoSlot(slot.getKey(), slot.getValue());
	}

	public String getUserId() {
		return userId;
	}

	public List<DynamoSlot> getSlots() {
		return slots;
	}

	public long getSkipTimeInSeconds() {
		return skipTimeInSeconds;
	}

	public Quest getDailyQuest() {
		return dailyQuest;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSlots(List<DynamoSlot> slots) {
		this.slots = slots;
	}

	public void setSkipTimeInSeconds(long skipTimeInSeconds) {
		this.skipTimeInSeconds = skipTimeInSeconds;
	}

	public void setDailyQuest(Quest dailyQuest) {
		this.dailyQuest = dailyQuest;
	}

	@Override
	public String getId() {
		return userId;
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}
}
