package com.etermax.spacehorse.core.achievements.model;

public enum AchievementType {

	PURCHASE_CAPTAIN("PurchaseCaptain"), //
	PLAY_BATTLE("PlayBattle"),//
	COMPLETE_AT_LEAST_ONE_DAILY_QUEST("CompleteDailyQuest"),//
	COMPLETE_AT_LEAST_ONE_HARD_QUEST("CompleteHardQuest"),//
	PURCHASE_CARD("PurchaseCard"), //
	UNLOCK_CARD("UnlockCard"), //
	REACH_LEVEL("ReachLevel"), //
	COMPLETE_AT_LEAST_ONE_SPEEDUP_COURIER("SpeedupCourier"), //
	REACH_STADIUM("ReachStadium"), //
	PURCHASE_SKIN("BuySkin"),
	PLAY_AT_LEAST_ONE_FRIENDLY_BATTLE("PlayFriendlyBattle");//

	private String type;

	AchievementType(String type) {
		this.type = type;
	}

	public String getTypeAsTxt() {
		return type;
	}
}