package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IosRequest {

    @JsonProperty("receipt-data")
    private String receiptData;

    public IosRequest(@JsonProperty("receipt-data") String receiptData) {
        this.receiptData = receiptData;
    }

    public String getReceiptData() {
        return receiptData;
    }

}
