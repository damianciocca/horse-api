package com.etermax.spacehorse.core.catalog.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestCatalogsRequest {
    @JsonProperty("adminLoginId")
    private String adminLoginId;
}
