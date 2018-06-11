package com.etermax.spacehorse.core.inapps.domain.market.android;

import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

public class AndroidReceipt extends Receipt {

    @JsonProperty("Store")
    private String store;

	@JsonProperty("TransactionID")
    private String transactionId;

    @JsonProperty("Payload")
    private AndroidReceiptPayload payload;

	public AndroidReceipt(@JsonProperty("Store") String store,
            @JsonProperty("TransactionID") String transactionId,
			@JsonProperty("Payload") AndroidReceiptPayload payload) {
		this.store = store;
		this.transactionId = transactionId;
		this.payload = payload;
	}

	public AndroidReceipt(Object receipt) {
		try {
		    String value = ((String) receipt);
		    value = value.replaceAll("\\\\\\\\\"", "\"");
            value = value.replaceAll("\\\\\"", "\"");
            value = value.replace("\"{", "{");
            value = value.replace("}\"", "}");
			AndroidReceipt androidReceipt = new ObjectMapper().readValue(value, AndroidReceipt.class);
			Validate.notNull(androidReceipt);
            Validate.notNull(androidReceipt.payload);
			Validate.notNull(androidReceipt.payload.getData());
            Validate.notNull(androidReceipt.payload.getData().getProductId());
            Validate.notNull(androidReceipt.payload.getSignature());
            this.store = androidReceipt.store;
            this.transactionId = androidReceipt.transactionId;
            this.payload = androidReceipt.payload;
		} catch (Exception e) {
		    this.setValid(false);
		}
	}

    @JsonIgnore
	public String getSignature() {
        if (this.payload != null && this.payload.getSignature() != null) {
            return this.payload.getSignature();
        }
		return null;
	}

    @JsonIgnore
	public String getPackageName(){
		if (this.payload != null && this.payload.getData() != null) {
			return (String) this.payload.getData().getPackageName();
		}
		return null;
	}

    @JsonIgnore
	@Override
	public String getProductId() {
		if (this.payload != null && this.payload.getData() != null) {
			return this.payload.getData().getProductId();
		}
		return null;
	}

    @JsonIgnore
	public void setProductId(String productId) {
		if (this.payload != null && this.payload.getData() != null) {
			this.payload.getData().setProductId(productId);
		}
	}

	@JsonIgnore
	public String getDataAsString() {
		if (this.payload != null) {
			return this.payload.getDataAsString();
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

    public AndroidReceiptPayload getPayload() {
        return payload;
    }

    public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
