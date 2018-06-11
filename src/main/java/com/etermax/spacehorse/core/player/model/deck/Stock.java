package com.etermax.spacehorse.core.player.model.deck;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.List;

@DynamoDBDocument
public class Stock {
    @DynamoDBAttribute(attributeName = "stockId")
    private int stockId;

    @DynamoDBAttribute(attributeName = "selectedCardsIds")
    private List<Long> selectedCardsIds;

    public Stock(Integer stockId, List<Long> selectedCardsIds){
        this.stockId = stockId;
        this.selectedCardsIds = selectedCardsIds;
    }

    public Stock() {}

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public List<Long> getSelectedCardsIds() {
        return selectedCardsIds;
    }

    public void setSelectedCardsIds(List<Long> selectedCardsIds) {
        this.selectedCardsIds = selectedCardsIds;
    }
}
