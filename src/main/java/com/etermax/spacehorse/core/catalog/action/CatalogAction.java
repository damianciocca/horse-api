package com.etermax.spacehorse.core.catalog.action;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.CatalogABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.DynamoServerSettings;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogIsActive;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;

public class CatalogAction {

	private static final Logger logger = LoggerFactory.getLogger(CatalogAction.class);
	private static final int LIMIT_LIST_CATALOG = 10;

	private CatalogRepository catalogRepository;
	private ServerSettingsRepository serverSettingsRepository;

	public CatalogAction(CatalogRepository catalogRepository, ServerSettingsRepository serverSettingsRepository) {
		this.catalogRepository = catalogRepository;
		this.serverSettingsRepository = serverSettingsRepository;
	}

	public String createCatalog(CatalogResponse catalogResponse) {
		Catalog catalog = new Catalog(catalogResponse);
		catalogRepository.add(catalog);
		return catalog.getCatalogId();
	}

	public Optional<Catalog> findById(String id) {
		return Optional.ofNullable(catalogRepository.find(id));
	}

	public List<CatalogIsActive> listLastCatalogs() {
		List<CatalogIsActive> latestCatalogs = catalogRepository.listLatestCatalogs(LIMIT_LIST_CATALOG);
		latestCatalogs.forEach(catalogIsActive -> logger.info("Catalog => {} ", catalogIsActive));
		updateIsActive(latestCatalogs);
		return latestCatalogs;
	}

	private void updateIsActive(List<CatalogIsActive> latestCatalogs) {
		DynamoServerSettings serverSettings = serverSettingsRepository.getServerSettings();
		latestCatalogs.stream().filter(x -> x.getCatalogId().equals(serverSettings.getCatalogId())).findFirst()
				.ifPresent(catalogIsActive -> catalogIsActive.setActive(true));
	}

	public void setActiveCatalog(String catalogId) {
		serverSettingsRepository.setActiveCatalogId(catalogId);
	}

	public Catalog getCatalogForUser(ABTag userTag) {
		return catalogRepository.getActiveCatalogWithTag(userTag);
	}

	public Optional<Catalog> findByIdWithTag(String catalogIdWithTag) {
		CatalogABTag catalogABTag = CatalogABTag.buildFromCatalogWithTag(catalogIdWithTag);
		return Optional.ofNullable(catalogRepository.findByIdWithTag(catalogABTag.getCatalogId(), catalogABTag.getAbTag()));
	}

	public void validateCreatedCatalog(String catalogId) {
		Catalog catalog = catalogRepository.find(catalogId);
		catalog.getAbTesterCollection().getEntries().forEach(abTesterEntry -> {
			ABTag abTag = new ABTag(abTesterEntry.getId());
			if (StringUtils.isBlank(abTesterEntry.getDeltas())) {
				return;
			}
			Catalog modifiedCatalog = catalogRepository.findByIdWithTag(catalogId, abTag);
			modifiedCatalog.validate();
		});
	}
}
