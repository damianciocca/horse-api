package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class CatalogEntry {

	private final String id;

	public String getId() {
		return id;
	}

	public CatalogEntry(String id) {
		this.id = id;
	}

	public abstract void validate(Catalog catalog);

	protected void validateParameter(boolean condition, String errorMessage) {
		if (!condition) {
			throw new CatalogException(
					"Error validating " + getClass().getSimpleName() + " " + getId() + ": " + errorMessage);
		}
	}

	protected void validateParameter(boolean condition, String errorMessage, Object... errorMessageArgs) {
		if (!condition) {
			throw new CatalogException(
					"Error validating " + getClass().getSimpleName() + " " + getId() + ": " + String.format(errorMessage, errorMessageArgs));
		}
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogEntry that = (CatalogEntry) o;
        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

}
