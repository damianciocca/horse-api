package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogEntryNotFound;

import java.util.*;

public class CatalogEntriesCollection<T extends CatalogEntry> {

	private final List<T> entries = new ArrayList<>();

	private transient Map<String, T> entriesById = new HashMap<>();

	public CatalogEntriesCollection() {
	}

	public CatalogEntriesCollection(List<T> entries) {
		if (entries == null) {
			entries = new ArrayList<>();
		}
		this.entries.addAll(entries);
		this.entries.forEach(t -> entriesById.put(t.getId(), t));
	}

	public List<T> getEntries() {
		return entries;
	}

	public void addEntry(T entry) {
		entries.add(entry);
		entriesById.put(entry.getId(), entry);
	}

	public Optional<T> findById(String id) {
		return Optional.ofNullable(entriesById.get(id));
	}

	public T findByIdOrFail(String id) {
		return findById(id).orElseThrow(() -> new CatalogEntryNotFound("Entry " + id + " not found in " + getClass().getName()));
	}

    public boolean containsDuplicates() {
        List<T> entries = this.getEntries();
        final HashSet hashSet = new HashSet();
        for (CatalogEntry entry : entries) {
            if (! hashSet.add(entry)) {
                return true;
            }
        }
        return false;
    }

    public String getClassName() {
        try {
            return this.getEntries().get(0).getClass().getName();
        } catch (Exception e) {
            return this.getClass().getName();
        }
    }

}
