package com.etermax.spacehorse.core.catalog.filter;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.CatalogABTag;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.status;

@RequiresLatestCatalog
public class RequiresLatestCatalogFilter implements ContainerRequestFilter {

	public static final String CATALOG_ID = "Catalog-Id";
	public static final String CLIENT_VERSION = "Client-Version";
	public static final String CLIENT_PLATFORM = "Client-Platform";

	private CatalogRepository catalogRepository;

	public RequiresLatestCatalogFilter(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		RequestCatalogInfo requestInfo = getRequestCatalogInfoFromHeaders(requestContext);

		if (requestInfo != null) {
			if (!catalogRepository.getActiveCatalogWithTag(requestInfo.getABTag()).getCatalogId().equals(requestInfo.getCatalogId())) {
				requestContext.abortWith(status(Status.CONFLICT).entity("Catalog changed, please reload").build());
			}
		}
	}

	private RequestCatalogInfo getRequestCatalogInfoFromHeaders(ContainerRequestContext requestContext) {
		String catalogIdWithTag = requestContext.getHeaderString(CATALOG_ID);
		String clientVersion = requestContext.getHeaderString(CLIENT_VERSION);
		String clientPlatform = requestContext.getHeaderString(CLIENT_PLATFORM);

		if (catalogIdWithTag != null && clientVersion != null && clientPlatform != null) {
			CatalogABTag catalogABTag = CatalogABTag.buildFromCatalogWithTag(catalogIdWithTag);
			String catalogId = catalogABTag.getCatalogId();
			ABTag abTag = catalogABTag.getAbTag();
			return new RequestCatalogInfo(catalogId, clientPlatform, clientVersion, abTag);
		}

		return null;
	}
}
