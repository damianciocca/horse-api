package com.etermax.spacehorse.core.abtest.model;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.dynamo.TagDoesntExistException;

public class ABTagUtils {
	static public boolean getAbTagBattleCompatible(CatalogRepository catalogRepository, String abTagString) {

		ABTag abTag = new ABTag(abTagString);

		if (abTag.isEmptyABTag())
			return true;

		Catalog catalog = catalogRepository.getActiveCatalogWithTag(ABTag.emptyABTag());

		try {
			return ABTestParser.getAbTesterEntry(catalog, abTag).getBattleCompatible();
		} catch (TagDoesntExistException ex) {
			return false;
		}
	}
}
