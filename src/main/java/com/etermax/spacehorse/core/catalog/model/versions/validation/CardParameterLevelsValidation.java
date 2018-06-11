package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.CardParameterLevelsCollection;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class CardParameterLevelsValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        CardParameterLevelsCollection cardParameterLevel = catalog.getCardParameterLevelsCollection();
        for (CardRarity rarity : CardRarity.values()) {
            int maxLevel = cardParameterLevel.getMaxCardLevelByRarity(rarity);
            if (maxLevel == 0) {
                throw new CatalogException("Missing levels for rarity " + rarity + " in " + "CardParameterLevels");
            }

            for (int level = 0; level < maxLevel; level++) {
                if (!cardParameterLevel.findByRarityAndLevel(rarity, level).isPresent()) {
                    throw new CatalogException("Missing level " + level + " for rarity " + rarity + " in " + "CardParameterLevels");
                }
            }
        }
        return true;
    }

}
