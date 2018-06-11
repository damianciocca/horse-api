package com.etermax.spacehorse.core.player.model.stats;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.catalog.model.Catalog;

@DynamoDBDocument
public class MatchStats {

	@DynamoDBAttribute(attributeName = "multiplayerPlayed")
	private int multiplayerPlayed;

	@DynamoDBAttribute(attributeName = "multiplayerWon")
	private int multiplayerWon;

	@DynamoDBAttribute(attributeName = "multiplayerLost")
	private int multiplayerLost;

    @DynamoDBAttribute(attributeName = "multiplayerWonFullScore")
    private int multiplayerWonFullScore;

    @DynamoDBAttribute(attributeName = "opponentShipsDestroyed")
	private int opponentShipsDestroyed;

	public MatchStats() {
	}

	public int getMultiplayerPlayed() {
		return multiplayerPlayed;
	}

	public void setMultiplayerPlayed(int multiplayerPlayed) {
		this.multiplayerPlayed = multiplayerPlayed;
	}

	public int getMultiplayerWon() {
		return multiplayerWon;
	}

	public void setMultiplayerWon(int multiplayerWon) {
		this.multiplayerWon = multiplayerWon;
	}

	public int getMultiplayerLost() {
		return multiplayerLost;
	}

	public void setMultiplayerLost(int multiplayerLost) {
		this.multiplayerLost = multiplayerLost;
	}

    public int getMultiplayerWonFullScore() {
        return multiplayerWonFullScore;
    }

    public void setMultiplayerWonFullScore(int multiplayerWonFullScore) {
        this.multiplayerWonFullScore = multiplayerWonFullScore;
    }

    public void incrementMultiplayerPlayed() {
		multiplayerPlayed++;
	}

	public void incrementMultiplayerWon() {
		multiplayerWon++;
	}

	public void incrementMultiplayerLost() {
		multiplayerLost++;
	}

	public void incrementMultiplayerWonFullScore() {
        multiplayerWonFullScore++;
    }

	public void checkAndFixIntegrity(Catalog catalog) {
	}

	public int getOpponentShipsDestroyed() {
		return opponentShipsDestroyed;
	}

	public void incrementShipsDestroyed() { opponentShipsDestroyed++; }

	public void setOpponentShipsDestroyed(int opponentShipsDestroyed) {
		this.opponentShipsDestroyed = opponentShipsDestroyed;
	}
}
