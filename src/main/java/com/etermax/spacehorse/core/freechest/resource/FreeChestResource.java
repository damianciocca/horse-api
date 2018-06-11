package com.etermax.spacehorse.core.freechest.resource;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.freechest.action.OpenFreeChestAction;
import com.etermax.spacehorse.core.freechest.resource.request.OpenFreeChestRequest;
import com.etermax.spacehorse.core.freechest.resource.response.OpenFreeChestResponse;

import io.swagger.annotations.Api;

@Path("/v1/player/chest")
@Api(value = "/chest", tags = "Chest")
public class FreeChestResource {

	private OpenFreeChestAction openFreeChestAction;

	public FreeChestResource(OpenFreeChestAction openFreeChestAction) {
		this.openFreeChestAction = openFreeChestAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("openFree")
	public Response openFreeChest(@Context HttpServletRequest request, OpenFreeChestRequest openFreeChestRequest) {
		OpenFreeChestResponse response = openFreeChestAction.openFreeChest(request.getHeader("Login-Id"));
		return Response.ok(response).build();
	}

}
