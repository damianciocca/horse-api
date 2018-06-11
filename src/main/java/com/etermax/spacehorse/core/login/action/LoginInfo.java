package com.etermax.spacehorse.core.login.action;

import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.abtest.model.CatalogABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.model.User;

public class LoginInfo {

	private User user;
	private Catalog catalog;
	private Player player;
	private String password;
	private int mmr;
	private String linkedWithSocialAccountId;
	private Optional<PlayerLeague> playerSeasons;

	public LoginInfo(User user, Player player, String password, int mmr, String linkedWithSocialAccountId, Catalog catalog,
			Optional<PlayerLeague> playerSeasons) {
		this.user = user;
		this.catalog = catalog;
		this.player = player;
		this.password = password;
		this.mmr = mmr;
		this.linkedWithSocialAccountId = linkedWithSocialAccountId;
		this.playerSeasons = playerSeasons;
	}

	private String getCatalogWithTag(String latestCatalogId, Player player) {
		if (player == null) {
			return latestCatalogId;
		}
		return new CatalogABTag(latestCatalogId, player.getAbTag()).toString();
	}

	public User getUser() {
		return user;
	}

	public String getLatestCatalogId() {
		return getCatalogWithTag(catalog.getCatalogId(), player);
	}

	public Player getPlayer() {
		return player;
	}

	public String getPassword() {
		return password;
	}

	public int getMmr() {
		return mmr;
	}

	public String getLinkedWithSocialAccountId() {
		return linkedWithSocialAccountId;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public List<SpecialOfferDefinition> getSpecialOfferDefinitions() {
		return getCatalog().getSpecialOfferDefinitionsCollection().getEntries();
	}

	public Optional<PlayerLeague> getPlayerSeasons() {
		return playerSeasons;
	}
}
