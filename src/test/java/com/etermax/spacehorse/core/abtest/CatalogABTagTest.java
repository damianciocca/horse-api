package com.etermax.spacehorse.core.abtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.CatalogABTag;

public class CatalogABTagTest {

	@Test
	public void whenBuildFromCatalogWithTag() {
		String catalogWithTag = "123-campaignId-segmentId";
		CatalogABTag catalogABTag = CatalogABTag.buildFromCatalogWithTag(catalogWithTag);
		assertThat(catalogABTag.getCatalogId()).isEqualTo("123");
		assertThat(catalogABTag.getAbTag()).isEqualTo(new ABTag("campaignId", "segmentId"));
	}

	@Test
	public void whenBuildFromCatalogWithNoTag() {
		String catalogWithTag = "123";
		CatalogABTag catalogABTag = CatalogABTag.buildFromCatalogWithTag(catalogWithTag);
		assertThat(catalogABTag.getCatalogId()).isEqualTo("123");
		assertThat(catalogABTag.getAbTag().getCampaignId()).isEqualTo("");
		assertThat(catalogABTag.getAbTag().getSegmentId()).isEqualTo("");
	}
}
