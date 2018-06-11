package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.Set;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class VideoRewardPlacementsValidator implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {
		Set<String> videoRewardPlacements = catalog.getVideoRewardDefinitionsCollection().getEntries().stream()
				.map(VideoRewardDefinition::getPlaceName).collect(Collectors.toSet());
		catalog.getGameConstants().getVideoRewardPlacementsToValidate().forEach(videoRewardPlacement -> {
			if (!videoRewardPlacements.contains(videoRewardPlacement)) {
				throw new CatalogException("Video rewards with placement " + videoRewardPlacement + " is required");
			}
		});
		return true;
	}

}
