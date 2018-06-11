package com.etermax.spacehorse.core.abtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;

public class ABTagTest {

	@Test
	public void whenEmtpyABTagCreatedSegmentAndCampaignAreEmpty() {
		ABTag emptyABTag = ABTag.emptyABTag();
		assertThat(emptyABTag.getSegmentId()).isEqualTo("");
		assertThat(emptyABTag.getCampaignId()).isEqualTo("");
	}

	@Test
	public void whenBuildFromPostulationEmtpyABTagIsEmtpy() {
		ABTag abTag = ABTag.buildFromPostulation(Optional.empty());
		assertThat(abTag.getSegmentId()).isEqualTo("");
		assertThat(abTag.getCampaignId()).isEqualTo("");
	}
}
