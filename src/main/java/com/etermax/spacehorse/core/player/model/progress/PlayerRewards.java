package com.etermax.spacehorse.core.player.model.progress;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.etermax.spacehorse.core.catalog.exception.CatalogEntryNotFound;
import com.etermax.spacehorse.core.catalog.model.*;

import java.util.List;
import java.util.Optional;

@DynamoDBDocument
public class PlayerRewards {

    @DynamoDBTypeConverted(converter = SequenceTypeConverter.class)
    @DynamoDBAttribute(attributeName = "chestRewardSequence")
	private Sequence chestRewardSequence;

    @DynamoDBTypeConverted(converter = SequenceTypeConverter.class)
    @DynamoDBAttribute(attributeName = "freeChestGemsRewardSequence")
    private Sequence freeChestGemsRewardSequence;

    @DynamoDBAttribute(attributeName = "rewardsReceivedToday")
    private RewardsReceivedToday rewardsReceivedToday;

	public PlayerRewards() {
        this.chestRewardSequence = new Sequence();
        this.freeChestGemsRewardSequence = new Sequence();
        this.rewardsReceivedToday = new RewardsReceivedToday();
	}

    public PlayerRewards(long timeNowInSeconds) {
        this.chestRewardSequence = new Sequence();
        this.freeChestGemsRewardSequence = new Sequence();
        this.rewardsReceivedToday = new RewardsReceivedToday(timeNowInSeconds);
    }

    public void checkAndFixIntegrity(Catalog catalog, long timeNowInSeconds) {
        this.checkChestRewardSequence(catalog);
        this.checkFreeChestGemsRewardSequence(catalog);
        if (rewardsReceivedToday == null) {
            rewardsReceivedToday = new RewardsReceivedToday(timeNowInSeconds);
        }
	}

    private void checkChestRewardSequence(Catalog catalog) {
        if (chestRewardSequence == null) {
            chestRewardSequence = new Sequence();
        }
        String sequenceId = catalog.getGameConstants().getDefaultChestRewardSequenceId();
        Optional<ChestList> chestListOptional = catalog.getChestsListsCollection().findById(sequenceId);
        chestRewardSequence.scaleOrder(chestListOptional.get().getEntries().size());
    }

    private void checkFreeChestGemsRewardSequence(Catalog catalog) {
        if (freeChestGemsRewardSequence == null) {
            freeChestGemsRewardSequence = new Sequence();
        }
        String sequenceId = catalog.getGameConstants().getDefaultFreeGemsCycleSequenceId();
        Optional<GemsCycle> gemsCycleOptional = catalog.getGemsCycleCollection().findById(sequenceId);
        freeChestGemsRewardSequence.scaleOrder(gemsCycleOptional.get().getEntries().size());
    }

    public ChestDefinition getNextChestReward(String sequenceId, CatalogEntriesCollection<ChestList> chestsListsCatalog,
            CatalogEntriesCollection<ChestDefinition> chestDefinitionsCatalog) {

        List<ChestListEntry> chestList = chestsListsCatalog.findById(sequenceId).get().getEntries();
        chestRewardSequence.scaleOrder(chestList.size());
        String chestId = chestList.stream()
                .filter(chestListEntry -> chestListEntry.getSequenceOrder() == chestRewardSequence.getOrder())
                .findFirst().get().getChestId();
        ChestDefinition chestDefinition = chestDefinitionsCatalog.findById(chestId).get();
        chestRewardSequence.increaseOrder();
        return chestDefinition;
    }

    public int getNextFreeChestGemsRewardAmount(Catalog catalog) {
        String sequenceId = catalog.getGameConstants().getDefaultFreeGemsCycleSequenceId();
        List<GemsCycleEntry> gemsCycleList = catalog.getGemsCycleCollection().findById(sequenceId).get().getEntries();
        freeChestGemsRewardSequence.scaleOrder(gemsCycleList.size());
        int freeGemsAmount = gemsCycleList.stream()
                .filter(gce -> gce.getSequenceOrder() == freeChestGemsRewardSequence.getOrder())
                .findFirst().get().getFreeGemsAmount();
        freeChestGemsRewardSequence.increaseOrder();
        return freeGemsAmount;
    }

    public Sequence getChestRewardSequence() {
        return chestRewardSequence;
    }

    public void setChestRewardSequence(Sequence chestRewardSequence) {
        this.chestRewardSequence = chestRewardSequence;
    }

    public Sequence getFreeChestGemsRewardSequence() {
        return freeChestGemsRewardSequence;
    }

    public void setFreeChestGemsRewardSequence(Sequence freeChestGemsRewardSequence) {
        this.freeChestGemsRewardSequence = freeChestGemsRewardSequence;
    }

    public RewardsReceivedToday getRewardsReceivedToday() {
        return rewardsReceivedToday;
    }

    public void setRewardsReceivedToday(RewardsReceivedToday rewardsReceivedToday) {
        this.rewardsReceivedToday = rewardsReceivedToday;
    }

}
