package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.UnitDefinition;
import com.etermax.spacehorse.core.catalog.model.UnitLevelDefinition;
import com.etermax.spacehorse.core.catalog.model.UnitType;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class MothershipsAndCompanionshipsMoveSpeedConsistencyValidation implements CatalogValidation {
	@Override
	public boolean validate(Catalog catalog) {

		long moveSpeed = 0;
		boolean moveSpeedInitialized = false;

		for (UnitDefinition unitDefinition : catalog.getUnitDefinitionsCollection().getEntries()) {

			if (isMothershipOrCompanionship(unitDefinition)) {

				List<UnitLevelDefinition> levels = getUnitLevels(catalog, unitDefinition);

				for (UnitLevelDefinition level : levels) {

					if (!moveSpeedInitialized) {
						moveSpeed = level.getMoveSpeed();
						moveSpeedInitialized = true;
					}

					if (level.getMoveSpeed() != moveSpeed) {
						throw new CatalogException(
								"Unit " + unitDefinition.getId() + " has a different move speed than the other motherships / companionships");
					}
				}
			}

		}

		return false;
	}

	private List<UnitLevelDefinition> getUnitLevels(Catalog catalog, UnitDefinition unitDefinition) {
		return catalog.getUnitLevelDefinitionsCollection().getEntries().stream().filter(x -> x.getUnitId().equals(unitDefinition.getId()))
				.collect(Collectors.toList());
	}

	private boolean isMothershipOrCompanionship(UnitDefinition unitDefinition) {
		return unitDefinition.getUnitType() == UnitType.MOTHERSHIP || unitDefinition.getUnitType() == UnitType.COMPANIONSHIP;
	}
}
