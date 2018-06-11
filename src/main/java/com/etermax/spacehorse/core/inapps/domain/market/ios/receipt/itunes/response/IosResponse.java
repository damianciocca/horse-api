package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IosResponse {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("receipt")
    private IosReceiptResponse receipt;

    public IosResponse(@JsonProperty("status") Integer status, @JsonProperty("receipt") IosReceiptResponse receipt) {
        this.status = status;
        this.receipt = receipt;
    }

    public Integer getStatus() {
        return status;
    }

	public IosReceiptResponse getReceipt() {
		return receipt;
	}

}
