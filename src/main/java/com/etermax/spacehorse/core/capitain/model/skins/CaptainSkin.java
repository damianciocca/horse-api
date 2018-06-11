package com.etermax.spacehorse.core.capitain.model.skins;

import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

public class CaptainSkin {

	private final String captainSkinId;
	private final CaptainSkinDefinition captainSkinDefinition;

	public CaptainSkin(String captainSkinId, CaptainSkinDefinition captainSkinDefinition) {
		this.captainSkinId = captainSkinId;
		this.captainSkinDefinition = captainSkinDefinition;
	}

	public String getCaptainSkinId() {
		return captainSkinId;
	}

	public int getSlotNumber() {
		return captainSkinDefinition.getSlotNumber();
	}

	public String getCaptainId() {
		return captainSkinDefinition.getCaptainId();
	}

	public int getGemPrice() {
		return captainSkinDefinition.getGemsPrice();
	}

	public int getGoldPrice() {
		return captainSkinDefinition.getGoldPrice();
	}

	public String getSkindId() {
		return captainSkinDefinition.getSkinId();
	}

	public boolean isDefault() {
		return captainSkinDefinition.isDefault();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CaptainSkin that = (CaptainSkin) o;

		return captainSkinId != null ? captainSkinId.equals(that.captainSkinId) : that.captainSkinId == null;
	}

	@Override
	public int hashCode() {
		return captainSkinId != null ? captainSkinId.hashCode() : 0;
	}
}
