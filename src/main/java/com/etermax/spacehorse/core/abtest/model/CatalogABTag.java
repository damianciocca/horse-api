package com.etermax.spacehorse.core.abtest.model;

import org.apache.commons.lang3.StringUtils;

import com.etermax.spacehorse.core.error.BlanckCatalogIdWithTagException;

public class CatalogABTag {
	private String catalogId;
	private ABTag abTag;

	private CatalogABTag(String catalogWithTag) {
		if(StringUtils.isBlank(catalogWithTag)){
			throw new BlanckCatalogIdWithTagException();
		}
		String[] splitedCatalogWithTag = catalogWithTag.split("-", 2);
		if(hasTagEmbedded(splitedCatalogWithTag)) {
			String abTag = splitedCatalogWithTag[1];
			this.abTag = new ABTag(abTag);
		} else {
			this.abTag = ABTag.emptyABTag();
		}
		this.catalogId = splitedCatalogWithTag[0];
	}

	public CatalogABTag(String catalogId, ABTag abTag) {
		if(StringUtils.isBlank(catalogId)) {
			this.catalogId = "";
		} else {
			this.catalogId = catalogId;
		}
		if(abTag == null) {
			this.abTag = ABTag.emptyABTag();
		} else {
			this.abTag = abTag;
		}
	}

	private boolean hasTagEmbedded(String[] splitedCatalogWithTag) {
		return splitedCatalogWithTag.length == 2;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public ABTag getAbTag() {
		return abTag;
	}

	public static CatalogABTag buildFromCatalogWithTag(String catalogWithTag) {
		return new CatalogABTag(catalogWithTag);
	}

	@Override
	public String toString() {
		return this.catalogId + "-" + this.abTag.toString();
	}
}
