package com.etermax.spacehorse.core.capitain.model.slots;

import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;

public class CaptainSlot {

	static public final int MAX_SLOT_NUMBER = 3;

	private final int slotNumber;
	private final CaptainSkin captainSkin;

	public CaptainSlot(int slotNumber, CaptainSkin captainSkin) {
		this.slotNumber = slotNumber;
		this.captainSkin = captainSkin;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public CaptainSkin getCaptainSkin() {
		return captainSkin;
	}

	public String getCaptainSkinId() {
		return captainSkin.getCaptainSkinId();
	}
}
