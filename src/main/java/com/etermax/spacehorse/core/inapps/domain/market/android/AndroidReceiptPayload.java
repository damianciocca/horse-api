package com.etermax.spacehorse.core.inapps.domain.market.android;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AndroidReceiptPayload {

    @JsonProperty("json")
    private AndroidReceiptPayloadData data;

    @JsonProperty("signature")
    private String signature;

    public AndroidReceiptPayload(@JsonProperty("json") AndroidReceiptPayloadData data,
            @JsonProperty("signature") String signature) {
        this.data = data;
        this.signature = signature;
    }

    public AndroidReceiptPayloadData getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }

    @JsonIgnore
    public String getDataAsString() {
        try {
            if (data != null) {
                return new ObjectMapper().writeValueAsString(data);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
}
