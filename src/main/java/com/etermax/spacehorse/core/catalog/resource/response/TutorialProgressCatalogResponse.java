package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Collectors;

public class TutorialProgressCatalogResponse {

    @JsonProperty("Id")
    private String tutorialId;

    @JsonProperty("ChestId")
    private String chestId;

    @JsonProperty("Gold")
    private int gold;

    @JsonProperty("Gems")
    private int gems;

    @JsonProperty("Cards")
    private String cards;

    @JsonProperty("BotCards")
    private String botCards;

    @JsonProperty("RandomEpic")
    private int randomEpic;

    @JsonProperty("FixedMMR")
    private int fixedMMR;

    @JsonProperty("Type")
    private String type;

    public TutorialProgressCatalogResponse() {
    }

    public TutorialProgressCatalogResponse(TutorialProgressEntry tutorialProgressEntry) {
        this.tutorialId = tutorialProgressEntry.getId();
        this.chestId = tutorialProgressEntry.getChestId();
        this.gems = tutorialProgressEntry.getGems();
        this.gold = tutorialProgressEntry.getGold();
        this.cards = tutorialProgressEntry.getCardsWithAmount().stream().map(cardWithAmount -> cardWithAmount.getCard().getCardType() + ":" + cardWithAmount
                .getAmount())
                .collect(Collectors.joining(","));
        this.botCards = tutorialProgressEntry.getBotCards().stream().map(card -> card.getCardType() + ":" + card.getLevel())
                .collect(Collectors.joining(","));
        this.randomEpic = tutorialProgressEntry.getRandomEpic();
        this.fixedMMR = tutorialProgressEntry.getFixedMMR();
        this.type = tutorialProgressEntry.getType();
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public String getChestId() {
        return chestId;
    }

    public int getGold() {
        return gold;
    }

    public int getGems() {
        return gems;
    }

    public String getCards() {
        return cards;
    }

    public int getRandomEpic() {
        return randomEpic;
    }

    public String getBotCards() { return botCards; }

    public int getFixedMMR() {
        return fixedMMR;
    }

    public String getType() {
        return type;
    }
}
