package com.etermax.spacehorse.core.login.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.login.resource.request.GetFeatureTogglesRequest;
import com.etermax.spacehorse.core.login.resource.response.GetFeatureTogglesResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/v1/featureToggles", tags = "Login")
@Path("/v1/featureToggles")
@Produces("application/json")
public class FeatureTogglesResource {

	public static final String AUTO_LOGIN = "AUTO_LOGIN";
	public static final String TUTORIAL_SPEEDUP = "TUTORIAL_SPEEDUP";
	public static final String SHOW_SPECIAL_OFFERS_POPUP = "SHOW_SPECIAL_OFFERS_POPUP";

	public FeatureTogglesResource() {
	}

	@POST
	@ApiOperation("Returns available feature toggles.")
	public Response featureToggles(GetFeatureTogglesRequest request) {
		GetFeatureTogglesResponse response = new GetFeatureTogglesResponse(new String[] { //
				AUTO_LOGIN, //
				TUTORIAL_SPEEDUP, //
				SHOW_SPECIAL_OFFERS_POPUP });
		return Response.ok(response).build();
	}
}
