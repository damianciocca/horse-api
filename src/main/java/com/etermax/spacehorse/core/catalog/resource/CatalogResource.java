package com.etermax.spacehorse.core.catalog.resource;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.csv.CatalogsCSVCollection;
import com.etermax.spacehorse.core.catalog.model.csv.exporter.CatalogCSVExporterJson;
import com.etermax.spacehorse.core.catalog.model.csv.importer.CatalogsCSVImporter;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.SheetsCollection;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.importer.SheetsImporterCSVs;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogIsActive;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogListResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CreateCatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SetActiveCatalogResponse;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;
import com.etermax.spacehorse.core.login.action.LoginAdminUserAction;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Api(value = "/catalog", tags = "Catalog")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource {

	private static final Logger logger = LoggerFactory.getLogger(CatalogResource.class);

	private final CatalogAction catalogAction;

	private final LoginAdminUserAction loginAction;

	private final boolean autoActivateNewCatalogs;

	public CatalogResource(CatalogAction catalogAction, LoginAdminUserAction loginAction, boolean autoActivateNewCatalogs) {
		this.catalogAction = catalogAction;
		this.loginAction = loginAction;
		this.autoActivateNewCatalogs = autoActivateNewCatalogs;
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/catalogCsv")
	@ApiOperation("Creates a new catalog, returning a HTTP201 including the created catalog id")
	public Response createCatalogCsv(@NotNull @FormParam("adminLoginId") String adminLoginId,
			@NotNull @FormParam("adminPassword") String adminPassword, @FormParam("csv") @NotEmpty String csv) throws URISyntaxException {
		loginAction.login(adminLoginId, adminPassword);

		logger.info("incoming csv {} ", csv);
		SheetsImporterCSVs sheetsImporter = new SheetsImporterCSVs();
		SheetsCollection sheets = sheetsImporter.importSheets(csv);
		CatalogsCSVImporter importer = new CatalogsCSVImporter();
		CatalogsCSVCollection catalogs = importer.processSheets(sheets);
		CatalogCSVExporterJson jsonExporter = new CatalogCSVExporterJson();
		String singleCatalogsJson = jsonExporter.export(catalogs);
		CatalogResponse catalogResponse;
		logger.info("incoming csv in json {} ", singleCatalogsJson);
		try {
			catalogResponse = new ObjectMapper().readValue(singleCatalogsJson, CatalogResponse.class);
			String catalogId = catalogAction.createCatalog(catalogResponse);
			catalogAction.validateCreatedCatalog(catalogId);
			if (autoActivateNewCatalogs) {
				catalogAction.setActiveCatalog(catalogId);
			}
			logger.info("catalog generated id {} ", catalogId);
			return Response.ok(new CreateCatalogResponse(catalogId)).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Response.serverError().entity(ErrorResponse.build(e.getMessage())).build();
		}
	}

	@GET
	@PermitAll
	@Path("/catalog/{catalogId}")
	@ApiOperation("Returns the catalog with the specified catalogId")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Login-Id", value = "Login-Id", required = true, dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "Session-Token", value = "Session-Token", required = true, dataType = "string", paramType = "header") })
	public Response getCatalog(@PathParam("catalogId") String catalogId) {
		Optional<Catalog> catalog = catalogAction.findByIdWithTag(catalogId);
		if (catalog.isPresent()) {
			return Response.ok(new CatalogResponse(catalog.get())).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/catalog/listLatestCatalogs")
	@ApiOperation("Returns the latest 10 catalogs")
	public Response getLatestCatalogs(@NotNull @FormParam("adminLoginId") String adminLoginId,
			@NotNull @FormParam("adminPassword") String adminPassword) {
		loginAction.login(adminLoginId, adminPassword);

		List<CatalogIsActive> catalogs = catalogAction.listLastCatalogs();
		if (!catalogs.isEmpty()) {
			return Response.ok(new CatalogListResponse(catalogs)).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/catalog/setActiveCatalogId")
	@ApiOperation("Set a catalog as active given a catalogId")
	public Response setActiveCatalog(@NotNull @FormParam("adminLoginId") String adminLoginId,
			@NotNull @FormParam("adminPassword") String adminPassword, @FormParam("catalogId") @NotEmpty String catalogId) {
		loginAction.login(adminLoginId, adminPassword);

		try {
			if (!catalogAction.findById(catalogId).isPresent()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			logger.error("Error while retrieving catalog with id " + catalogId, e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(ErrorResponse.build("The catalog couldn't be loaded, so it can't be set as the active catalog")).build();
		}

		catalogAction.setActiveCatalog(catalogId);
		return Response.ok(new SetActiveCatalogResponse()).build();
	}

}
