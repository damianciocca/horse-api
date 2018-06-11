package com.etermax.spacehorse.core.catalog.model.ads.videorewards;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.VideoRewardDefinitionResponse;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class VideoRewardCatalogMapper {

	public EntryContainerResponse<VideoRewardDefinitionResponse> mapFrom(Catalog catalog) {
		List<VideoRewardDefinitionResponse> responses = catalog.getVideoRewardDefinitionsCollection().getEntries().stream()
				.map(toVideoRewardDefinitionResponse()).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<VideoRewardDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getVideoRewardsCollection().getEntries().stream().map(toVideoRewardDefinition()).collect(Collectors.toList());
	}

	private Function<VideoRewardDefinitionResponse, VideoRewardDefinition> toVideoRewardDefinition() {
		return definitionResponse -> new VideoRewardDefinition( //
				definitionResponse.getId(), //
				definitionResponse.getPlaceName(),//
				definitionResponse.getTimeFrameInSeconds(), //
				definitionResponse.getMaxViewsPerTimeFrame(), //
				definitionResponse.getSpeedupTimeInSeconds(),//
				definitionResponse.getCoins(), definitionResponse.getMapNumber(),//
				mapFrom(definitionResponse.getType()),//
				definitionResponse.isMapFilterEnabled());
	}

	private Function<VideoRewardDefinition, VideoRewardDefinitionResponse> toVideoRewardDefinitionResponse() {
		return videoRewardDefinition -> new VideoRewardDefinitionResponse( //
				videoRewardDefinition.getId(), //
				videoRewardDefinition.getPlaceName(),//
				videoRewardDefinition.getTimeFrameInSeconds(), //
				videoRewardDefinition.getMaxViewsPerTimeFrame(),//
				videoRewardDefinition.getSpeedupTimeInSeconds(), //
				videoRewardDefinition.getCoins(),//
				videoRewardDefinition.getMapNumber(),//
				mapFrom(videoRewardDefinition.getType()),//
				videoRewardDefinition.isFilterMapEnabled());
	}

	private String mapFrom(VideoRewardType type) {
		return type.name();
	}

	private VideoRewardType mapFrom(String type) {
		try {
			return VideoRewardType.valueOf(type);
		} catch (Exception e) {
			throw new ApiException(
					"unexpected video reward type.. Expected [ " + VideoRewardType.SPEEDUP_TIME.name() + " ] or [ " + VideoRewardType.REWARD
							+ "]. Actual [ " + type + " " + "]");
		}
	}
}
