package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogIsActive {
    @JsonProperty("id")
    private String catalogId;

    @JsonProperty("active")
    private boolean isActive = false;

    public CatalogIsActive(String catalogId, boolean isActive) {
        this.catalogId = catalogId;
        this.isActive = isActive;
    }

    public CatalogIsActive() {
    }

    public String getCatalogId() {
        return catalogId;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
