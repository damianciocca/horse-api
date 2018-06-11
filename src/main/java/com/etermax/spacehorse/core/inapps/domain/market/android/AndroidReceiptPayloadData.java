package com.etermax.spacehorse.core.inapps.domain.market.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AndroidReceiptPayloadData {

    @JsonProperty
    private String orderId;

    @JsonProperty
    private String packageName;

    @JsonProperty
    private String productId;

    @JsonProperty
    private long purchaseTime;

    @JsonProperty
    private long purchaseState;

    @JsonProperty
    private String purchaseToken;

    public AndroidReceiptPayloadData() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public long getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(long purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
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
