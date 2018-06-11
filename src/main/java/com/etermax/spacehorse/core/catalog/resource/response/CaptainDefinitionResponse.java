package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("GoldPrice")
	private int goldPrice;

	@JsonProperty("GemsPrice")
	private int gemsPrice;

	public CaptainDefinitionResponse() {
	}

	public CaptainDefinitionResponse(String id, int mapNumber, int goldPrice, int gemsPrice) {
		this.id = id;
		this.mapNumber = mapNumber;
		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
	}

	public String getId() {
		return id;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getGoldPrice() {
		return goldPrice;
	}

	public int getGemsPrice() {
		return gemsPrice;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
