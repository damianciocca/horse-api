package com.etermax.spacehorse.core.quest.model;

public class QuestBoardConfiguration {

	private final long skipTimeInSeconds;
	private final int gemsCostToSkip;
	private final int nextQuestRemainingTimeDividerFactor;
	private final int nextQuestRemainingTimeGemsCostFactor;

	public QuestBoardConfiguration(long skipTimeInSeconds, int gemsCostToSkip, int nextQuestRemainingTimeDividerFactor,
			int nextQuestRemainingTimeGemsCostFactor) {
		this.skipTimeInSeconds = skipTimeInSeconds;
		this.gemsCostToSkip = gemsCostToSkip;
		this.nextQuestRemainingTimeDividerFactor = nextQuestRemainingTimeDividerFactor;
		this.nextQuestRemainingTimeGemsCostFactor = nextQuestRemainingTimeGemsCostFactor;
	}

	public long getSkipTimeInSeconds() {
		return skipTimeInSeconds;
	}

	public int getGemsCostToSkip() {
		return gemsCostToSkip;
	}

	public int getNextQuestRemainingTimeDividerFactor() {
		return nextQuestRemainingTimeDividerFactor;
	}

	public int getNextQuestRemainingTimeGemsCostFactor() {
		return nextQuestRemainingTimeGemsCostFactor;
	}
}
