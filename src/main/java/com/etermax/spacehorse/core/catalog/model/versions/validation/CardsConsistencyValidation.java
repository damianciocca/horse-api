package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class CardsConsistencyValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {

        for (CardDefinition cardDefinition : catalog.getCardDefinitionsCollection().getEntries()) {

            String unitId = cardDefinition.getUnitId();

            String powerUpId = cardDefinition.getPowerUpId();

            if (unitId.isEmpty() && powerUpId.isEmpty()) {
                throw new CatalogException("Card " + cardDefinition.getId() + " cannot have both UnitId and PowerUpId empty in CardsDefinition");
            }

            if (unitId.isEmpty() && powerUpId.isEmpty()) {
                throw new CatalogException("Card " + cardDefinition.getId() + " must have either UnitId or PowerUpId in CardsDefinition");
            }

            if (!unitId.isEmpty()) {
                if (!catalog.getUnitDefinitionsCollection().findById(unitId).isPresent()) {
                    throw new CatalogException("Card " + cardDefinition.getId() + " has UnitId not present in UnitDefinitions");
                }
            }

            if (!powerUpId.isEmpty()) {
                if (!catalog.getPowerUpDefinitionsCollection().findById(powerUpId).isPresent()) {
                    throw new CatalogException("Card " + cardDefinition.getId() + " has PowerUpId not present in PowerUpDefinitions");
                }
            }

            if (!catalog.getCardsDropRateCollection().getEntries().stream()
                    .filter(cardsDropRate -> cardsDropRate.getCardId().equals(cardDefinition.getId())).findFirst().isPresent()) {
                if (cardDefinition.getEnabled()) {
                    throw new CatalogException("Card " + cardDefinition.getId() + " is not present in CardDropRateDefinitions");
                }
            }

            if (!catalog.getCardLevelDefinitionsCollection().getEntries().stream()
                    .filter(cardLevelDefinition -> cardLevelDefinition.getCardId().equals(cardDefinition.getId())).findFirst().isPresent()) {
                if (cardDefinition.getEnabled()) {
                    throw new CatalogException("Card " + cardDefinition.getId() + " is not present in CardLevelsDefinitions");
                }
            }

        }

        return true;

    }

}
