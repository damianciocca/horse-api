package com.etermax.spacehorse.mock;

import java.util.List;

import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.google.common.collect.Lists;

public class CaptainSkinScenarioBuilder {

	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS_ID = "captain_hela_hela_arms_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD_ID = "captain_hela_hela_head_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST_ID = "captain_hela_hela_chest_1";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_ARMS2_ID = "captain_hela_hela_arms2_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_HEAD2_ID = "captain_hela_hela_head2_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_HELA_CHEST2_ID = "captain_hela_hela_chest2_1";

	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS_ID = "captain_rex_rex_arms_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD_ID = "captain_rex_rex_head_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST_ID = "captain_rex_rex_chest_1";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_ARMS2_ID = "captain_rex_rex_arms2_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_HEAD2_ID = "captain_rex_rex_head2_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_DROT_CHEST2_ID = "captain_rex_rex_chest2_1";

	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS_ID = "captain_jade_jade_arms_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD_ID = "captain_jade_jade_head_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST_ID = "captain_jade_jade_chest_1";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_ARMS2_ID = "captain_jade_jade_arms2_0";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_HEAD2_ID = "captain_jade_jade_head2_2";
	public static final String CAPTAIN_SKIN_DEFAULT_FOR_JADE_CHEST2_ID = "captain_jade_jade_chest2_1";

	private static final String DEFAULT_SKIN_ID = "default-skin-id";
	private static final boolean IS_DEFAULT = true;
	private List<CaptainSkin> captainSkins = Lists.newArrayList();
	private String captainId;

	public CaptainSkinScenarioBuilder(String captainId) {
		this.captainId = captainId;
	}

	public CaptainSkinScenarioBuilder withSkin(String captainSkinId, int slotNumber) {
		CaptainSkinDefinition definition = new CaptainSkinDefinition(captainSkinId, captainId, DEFAULT_SKIN_ID,
				new CaptainSkinDefinition.Slot(slotNumber), 0, 80, IS_DEFAULT);
		captainSkins.add(new CaptainSkin(captainSkinId, definition));
		return this;
	}

	public List<CaptainSkin> build() {
		return captainSkins;
	}

}
