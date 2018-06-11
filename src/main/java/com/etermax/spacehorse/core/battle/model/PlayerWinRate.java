package com.etermax.spacehorse.core.battle.model;

import static java.lang.String.format;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerWinRate {

	private static final Logger logger = LoggerFactory.getLogger(PlayerWinRate.class);

	private String userId;

	private int win = 0;

	private int lose = 0;

	private int tie = 0;

	private int mmr = 0;

	public PlayerWinRate(String userId) {
		this(userId, 0, 0, 0);
	}

	public PlayerWinRate(String userId, int win, int lose, int tie) {
		this(userId, win, lose, tie, 0);
	}

	public PlayerWinRate(String userId, int win, int lose, int tie, int mmr) {
		this.userId = userId;
		this.win = win;
		this.lose = lose;
		this.tie = tie;
		this.mmr = mmr;
	}

	public String getUserId() {
		return userId;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getTie() {
		return tie;
	}

	public void setTie(int tie) {
		this.tie = tie;
	}

	public double getWinRate() {
		if (this.getWin() + this.getLose() == 0) {
			return 0;
		}
		return (double) this.getWin() / (double) (this.getWin() + this.getLose());
	}

	public int getMmr() {
		return mmr;
	}

	public void updateMmr(int mmr) {
		this.mmr = mmr;
	}

	public void updateMmr(int mmr, int cappedMmr, boolean isCappedEnabled) {
		if (isCappedEnabled && mmr > cappedMmr) {
			logger.debug(format("for the user id [ %s ] the mmr [ %s ] was capped to [ %s ]", getUserId(), mmr, cappedMmr));
			this.mmr = cappedMmr;
		} else {
			this.mmr = mmr;
		}
	}

	public boolean tryToFixMmrWith(int cappedMmr, boolean isCappedEnabled) {
		if (isCappedEnabled && getMmr() > cappedMmr) {
			this.mmr = cappedMmr;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
