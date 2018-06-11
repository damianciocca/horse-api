package com.etermax.spacehorse.core.login.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.login.action.LinkGameCenterAction;
import com.etermax.spacehorse.core.login.action.LinkGooglePlayAction;
import com.etermax.spacehorse.core.player.model.Player;

public class DeleteAuthLink extends Cheat {

	private final LinkGooglePlayAction linkGooglePlayAction;
	private final LinkGameCenterAction linkGameCenterAction;

	public DeleteAuthLink(LinkGooglePlayAction linkGooglePlayAction, LinkGameCenterAction linkGameCenterAction) {
		this.linkGooglePlayAction = linkGooglePlayAction;
		this.linkGameCenterAction = linkGameCenterAction;
	}

	@Override
	public String getCheatId() {
		return "deleteAuthLink";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {

		linkGooglePlayAction.deleteLinkWithPlayerServices(player.getUserId());
		linkGameCenterAction.deleteLinkWithGameCenter(player.getUserId());

		return new CheatResponse("");
	}
}
