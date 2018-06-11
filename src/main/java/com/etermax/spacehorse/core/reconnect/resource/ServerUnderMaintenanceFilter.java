package com.etermax.spacehorse.core.reconnect.resource;

import static javax.ws.rs.core.Response.status;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalogFilter;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;

@Provider
public class ServerUnderMaintenanceFilter implements ContainerRequestFilter {

	private ServerSettingsRepository serverSettingsRepository;

	public ServerUnderMaintenanceFilter(ServerSettingsRepository serverSettingsRepository) {
		this.serverSettingsRepository = serverSettingsRepository;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (!isLoginRequest(requestContext) && isClientRequest(requestContext)) {

			if (isServerUnderMaintenance()) {
				requestContext.abortWith(
						status(Response.Status.CONFLICT).entity("Server under maintenance, please reload").build());
			}
		}
	}

	private boolean isClientRequest(ContainerRequestContext requestContext) {
		String catalogId = requestContext.getHeaderString(RequiresLatestCatalogFilter.CATALOG_ID);
		String clientVersion = requestContext.getHeaderString(RequiresLatestCatalogFilter.CLIENT_VERSION);
		String clientPlatform = requestContext.getHeaderString(RequiresLatestCatalogFilter.CLIENT_PLATFORM);

		return catalogId != null && clientVersion != null && clientPlatform != null;
	}

	private boolean isLoginRequest(ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPath().contains("login");
	}

	private boolean isServerUnderMaintenance() {
		return serverSettingsRepository.getServerSettings().isUnderMaintenance();
	}
}
