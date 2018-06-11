package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IosReceiptResponse {

	@JsonProperty("bundle_id")
	private String bundleId;

	@JsonProperty("application_version")
	private String applicationVersion;

	@JsonProperty("in_app")
	private List<IosReceiptInappResponse> inApps = new ArrayList<>();

	public IosReceiptResponse() {

	}

	public List<IosReceiptInappResponse> getInApps() {
		return inApps;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}
}
