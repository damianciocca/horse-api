package com.etermax.spacehorse.core.capitain.resource;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainsCollectionResponse {

	@JsonProperty("selectedCaptainId")
	private String selectedCaptainId;

	@JsonProperty("ownedCaptains")
	private List<CaptainResponse> ownedCaptains;

	public CaptainsCollectionResponse() {
		// just for jackson
	}

	public CaptainsCollectionResponse(CaptainsCollection captainsCollection) {
		List<Captain> captains = captainsCollection.getCaptains();
		this.ownedCaptains = captains.stream().map(CaptainResponse::new).collect(Collectors.toList());
		this.selectedCaptainId = captainsCollection.getSelectedCaptainId();
	}

	public String getSelectedCaptainId() {
		return selectedCaptainId;
	}

	public List<CaptainResponse> getOwnedCaptains() {
		return ownedCaptains;
	}
}
