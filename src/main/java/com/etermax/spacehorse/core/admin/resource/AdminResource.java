package com.etermax.spacehorse.core.admin.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.login.action.LoginAdminUserAction;
import com.etermax.spacehorse.core.user.action.UserAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1/admin")
@Api(value = "/admin", tags = "Admin")
public class AdminResource {

	private static final Logger logger = LoggerFactory.getLogger(AdminResource.class);

	private final UserAction userAction;
	private final LoginAdminUserAction loginAction;

	public AdminResource(UserAction userAction, LoginAdminUserAction loginAction) {
		this.userAction = userAction;
		this.loginAction = loginAction;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("updateRole")
	@ApiOperation("Change role PLAYER or TESTER for an specific player id. Only for ADMIN role.")
	public Response changeUserRole(ChangeRoleRequest request) {

		try {
			loginAction.login(request.getAdminLoginId(), request.getAdminPassword());
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		try {
			userAction.updateRole(request.getPlayerId(), request.getRole());
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Cannot change the role. [ " + e.getMessage() + " ]").build();
		}

		logger.info("Admin {} updated user {} with role {}", request.getAdminLoginId(), request.getPlayerId(), request.getRole());
		return Response.ok("Updated User " + request.getPlayerId() + " with role " + request.getRole()).build();
	}
}
