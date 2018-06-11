package com.etermax.spacehorse.core.catalog.model.versions.validation;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

import java.util.Optional;

public class CardsDropRateConsistencyValidation implements CatalogValidation {

    @Override
    public boolean validate(Catalog catalog) {
        for (CardsDropRate cardsDropRate : catalog.getCardsDropRateCollection().getEntries()) {
            Optional<CardDefinition> cardDefinitionOptional = catalog.getCardDefinitionsCollection().findById(cardsDropRate.getCardId());
            if (!cardDefinitionOptional.isPresent()) {
                throw new CatalogException("Drop Rate: Card Definition not found - " + cardsDropRate.getCardId());
            }
            boolean mapNumberNotExists = catalog.getMapsCollection().getEntries().stream()
                    .noneMatch(mapDefinition -> mapDefinition.getMapNumber() == cardsDropRate.getMapNumber());
            if (mapNumberNotExists) {
                throw new CatalogException("Drop Rate: Map number not found - " + cardsDropRate.getCardId());
            }
            if (cardDefinitionOptional.get().getEnabled()) {
                if (cardsDropRate.getMapNumber() < cardDefinitionOptional.get().getAvailableFromMapId()) {
                    throw new CatalogException("Drop Rate: Enabled card not available for map - " + cardDefinitionOptional.get().getId() + ", " + cardsDropRate.getMapNumber());
                }
            }
        }
        return true;
    }

}
