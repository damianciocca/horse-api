package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;

public class VideoRewardMapsValidator implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {
		Set<Integer> mapNumbers = catalog.getMapsCollection().getEntries().stream().map(MapDefinition::getMapNumber).collect(Collectors.toSet());
		Map<String, Integer> amountMapsByVideoRewardPlacement = new HashMap<>();

		catalog.getVideoRewardDefinitionsCollection().getEntries().forEach(videoRewardDefinition -> {
			String placeName = videoRewardDefinition.getPlaceName();
			validateVideoMapNumber(mapNumbers, videoRewardDefinition, placeName);
			accumulateAmountMapsByVideoRewardPlacement(amountMapsByVideoRewardPlacement, videoRewardDefinition, placeName);
		});
		validateAmountMapsByVideoRewardPlacement(mapNumbers, amountMapsByVideoRewardPlacement);
		return true;
	}

	private void validateAmountMapsByVideoRewardPlacement(Set<Integer> mapNumbers, Map<String, Integer> amountMapsByVideoRewardPlacement) {
		amountMapsByVideoRewardPlacement.keySet().forEach(videoRewardPlacement -> {
			Integer accumulatedAmountMapsForPlacement = amountMapsByVideoRewardPlacement.get(videoRewardPlacement);
			if (accumulatedAmountMapsForPlacement != mapNumbers.size()) {
				throw new CatalogException("Video rewards with placement " + videoRewardPlacement + " not contains all map numbers required");
			}
		});
	}

	private void accumulateAmountMapsByVideoRewardPlacement(Map<String, Integer> amountMapsByVideoRewardPlacement,
			VideoRewardDefinition videoRewardDefinition, String placeName) {
		if (videoRewardDefinition.isFilterMapEnabled()) {
			Integer accumulatedAmountMapsForPlacement = amountMapsByVideoRewardPlacement.get(placeName);
			if (accumulatedAmountMapsForPlacement == null) {
				amountMapsByVideoRewardPlacement.put(placeName, 1);
			} else {
				accumulatedAmountMapsForPlacement++;
				amountMapsByVideoRewardPlacement.put(placeName, accumulatedAmountMapsForPlacement);
			}
		}
	}

	private void validateVideoMapNumber(Set<Integer> mapNumbers, VideoRewardDefinition videoRewardDefinition, String placeName) {
		if (!mapNumbers.contains(videoRewardDefinition.getMapNumber())) {
			throw new CatalogException("Invalid map Number for video reward with placement " + placeName);
		}
	}

}
