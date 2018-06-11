package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class LeagueValidator implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {
		String leagueMapId = catalog.getGameConstants().getLeagueMapId();
		if (!existLeagueMapId(catalog, leagueMapId)) {
			throw new CatalogException("League map id in gameConstants is not valid");
		}
		return true;
	}

	private boolean existLeagueMapId(Catalog catalog, String leagueMapId) {
		return catalog.getMapsCollection().getEntries().stream().anyMatch(mapDefinition -> mapDefinition.getId().equals(leagueMapId));
	}
}
