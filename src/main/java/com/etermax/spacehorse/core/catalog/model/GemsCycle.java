package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

import java.util.List;

public class GemsCycle extends CatalogEntry {

    private final List<GemsCycleEntry> entries;

    public List<GemsCycleEntry> getEntries() {
        return entries;
    }

    public GemsCycle(String id, List<GemsCycleEntry> entries) {
        super(id);
        this.entries = entries;
    }

    @Override
    public void validate(Catalog catalog) {
        if (entries.size() == 0) {
            throw new CatalogException("Invalid Free Gems Cycle list");
        }
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSequenceOrder() != i) {
                throw new CatalogException("Invalid Sequence " + entries.get(i).getSequenceOrder() + " in Free Gems Cycle list " + getId());
            }
            if (entries.get(i).getFreeGemsAmount() < 0) {
                throw new CatalogException("Invalid Gems Amount " + entries.get(i).getFreeGemsAmount() + " in Free Gems Cycle list " + getId());
            }
        }
    }

}
