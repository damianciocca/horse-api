package com.etermax.spacehorse.core.capitain.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.capitain.actions.BuyCaptainAction;
import com.etermax.spacehorse.core.capitain.actions.SelectCaptainAction;
import com.etermax.spacehorse.core.capitain.actions.skins.BuyCaptainSkinAction;
import com.etermax.spacehorse.core.capitain.actions.skins.UpdateCaptainSkinsAction;
import com.etermax.spacehorse.core.capitain.resource.skins.BuyCaptainSkinRequest;
import com.etermax.spacehorse.core.capitain.resource.skins.BuyCaptainSkinResponse;
import com.etermax.spacehorse.core.capitain.resource.skins.UpdateCaptainSkinsRequest;
import com.etermax.spacehorse.core.capitain.resource.skins.UpdateCaptainSkinsResponse;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;

import io.swagger.annotations.Api;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/captain", tags = "Captain")
public class CaptainResource {

	private static final String LOGIN_ID = "Login-Id";

	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;
	private final BuyCaptainAction buyCaptainAction;
	private final SelectCaptainAction selectCaptainAction;
	private final BuyCaptainSkinAction buyCaptainSkinAction;
	private final UpdateCaptainSkinsAction updateCaptainSkinsAction;

	public CaptainResource(PlayerAction playerAction, CatalogAction catalogAction, BuyCaptainAction buyCaptainAction,
			SelectCaptainAction selectCaptainAction, BuyCaptainSkinAction buyCaptainSkinAction, UpdateCaptainSkinsAction updateCaptainSkinsAction) {
		this.playerAction = checkNotNull(playerAction);
		this.catalogAction = checkNotNull(catalogAction);
		this.buyCaptainAction = checkNotNull(buyCaptainAction);
		this.selectCaptainAction = selectCaptainAction;
		this.buyCaptainSkinAction = buyCaptainSkinAction;
		this.updateCaptainSkinsAction = updateCaptainSkinsAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/captain/buyCaptain")
	public Response buyCaptain(@Context HttpServletRequest httpServletRequest, BuyCaptainRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		buyCaptainAction.buyCaptain(player, request.getCaptainId(), catalog);
		return Response.ok(new BuyCaptainResponse()).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/captain/selectCaptain")
	public Response selectCaptain(@Context HttpServletRequest httpServletRequest, SelectCaptainRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		selectCaptainAction.selectCaptain(player, request.getCaptainId());
		return Response.ok(new SelectCaptainResponse()).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/captain/buyCaptainSkin")
	public Response buyCaptainSkin(@Context HttpServletRequest httpServletRequest, BuyCaptainSkinRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		buyCaptainSkinAction.buyCaptainSkin(player, request.getCaptainId(), request.getCaptainSkinId(), catalog);
		return Response.ok(new BuyCaptainSkinResponse()).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/captain/updateCaptainSkins")
	public Response updateCaptainSkins(@Context HttpServletRequest httpServletRequest, UpdateCaptainSkinsRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		Map<Integer, String> skinIdsBySlots = request.mapToSkinBySlots();
		updateCaptainSkinsAction.updateCaptainSkins(player, request.getCaptainId(), skinIdsBySlots, catalog);
		return Response.ok(new UpdateCaptainSkinsResponse()).build();
	}

}


