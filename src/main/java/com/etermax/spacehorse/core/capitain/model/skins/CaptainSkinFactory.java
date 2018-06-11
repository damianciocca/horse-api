package com.etermax.spacehorse.core.capitain.model.skins;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

public class CaptainSkinFactory {

	public CaptainSkin create(String captainSkinId, Catalog catalog) {
		CaptainSkinDefinition definition = catalog.getCaptainSkinDefinitionsCollection().findByIdOrFail(captainSkinId);
		return new CaptainSkin(definition.getId(), definition);
	}

	public CaptainSkin create(String captainSkinId, CaptainSkinDefinition definition) {
		return new CaptainSkin(captainSkinId, definition);
	}
}
