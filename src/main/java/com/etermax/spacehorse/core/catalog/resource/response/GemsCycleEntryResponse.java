package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.GemsCycle;
import com.etermax.spacehorse.core.catalog.model.GemsCycleEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GemsCycleEntryResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("ListId")
    private String listId;

    @JsonProperty("Sequence")
    private int sequence;

    @JsonProperty("FreeGemsAmount")
    private int freeGemsAmount;

    public String getId() {
        return id;
    }

    public String getListId() {
        return listId;
    }

    public int getSequence() {
        return sequence;
    }

    public int getFreeGemsAmount() {
        return freeGemsAmount;
    }

    public GemsCycleEntryResponse() {
    }

    public GemsCycleEntryResponse(GemsCycle gemsCycle, GemsCycleEntry entry) {
        this.id = gemsCycle.getId() + "-" + entry.getSequenceOrder();
        this.listId = gemsCycle.getId();
        this.sequence = entry.getSequenceOrder();
        this.freeGemsAmount = entry.getFreeGemsAmount();
    }

}
