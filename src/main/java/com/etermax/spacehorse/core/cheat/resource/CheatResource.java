package com.etermax.spacehorse.core.cheat.resource;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.cheat.action.CheatAction;
import com.etermax.spacehorse.core.cheat.resource.request.CheatRequest;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Api(value = "/cheat", tags = "Cheat")
public class CheatResource {

	private CheatAction cheatAction = null;

	public CheatResource(CheatAction cheatAction) {
		this.cheatAction = cheatAction;
	}

	@POST
	@RolesAllowed({ "TESTER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cheat")
	@ApiOperation("Applies the specified cheat. Only for Tester or Admin role.")
	public Response applyCheat(@Context HttpServletRequest request, CheatRequest cheatRequest) {
		CheatResponse response = cheatAction.applyCheat(request.getHeader("Login-Id"), cheatRequest);
		if (response != null) {
			return Response.ok(response).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.build("Invalid cheats")).build();
		}
	}

}
