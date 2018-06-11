package com.etermax.spacehorse.core.battle.model;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;

public class MmrCalculatorDomainService {

	private final CatalogRepository catalogRepository;

	public MmrCalculatorDomainService(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	public PlayersDeltaMmr calculate(BattlePlayer winnerPlayer, BattlePlayer loserPlayer) {
		Catalog latestCatalog = catalogRepository.getActiveCatalogWithTag(ABTag.emptyABTag());
		int mmrAlgorithmDefault = latestCatalog.getGameConstants().getMmrAlgorithmDefault();
		double mmrAlgorithmMultiplier = latestCatalog.getGameConstants().getMmrAlgorithmMultiplier();
		int minimumMMRAfterTutorial = latestCatalog.getGameConstants().getMinimumMMRAfterTutorial();
		DeltaMmrPercentageSelector deltaMmrPercentageSelector = latestCatalog.getDeltaMmrPercentageSelector();
		return new DeltaMmrAlgorithm(mmrAlgorithmDefault, mmrAlgorithmMultiplier, deltaMmrPercentageSelector, minimumMMRAfterTutorial)
				.calculate(winnerPlayer.getMmr(), loserPlayer.getMmr());
	}

}
