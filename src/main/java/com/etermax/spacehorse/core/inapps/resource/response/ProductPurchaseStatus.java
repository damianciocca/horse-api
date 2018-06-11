package com.etermax.spacehorse.core.inapps.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductPurchaseStatus {

	@JsonProperty("productId")
	private String productId;
	@JsonProperty("valid")
	private boolean valid;

	public ProductPurchaseStatus(@JsonProperty("productId") String productId, @JsonProperty("valid") boolean valid) {
		this.productId = productId;
		this.valid = valid;
	}

	public String getProductId() {
		return productId;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}