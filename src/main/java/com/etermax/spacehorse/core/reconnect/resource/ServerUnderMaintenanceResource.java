package com.etermax.spacehorse.core.reconnect.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.login.action.LoginAdminUserAction;

import io.swagger.annotations.Api;

@Path("/v1/server")
@Api(value = "/server", tags = "Admin")
public class ServerUnderMaintenanceResource {
	private ManageServerStatusAction manageServerStatusAction;
	private LoginAdminUserAction loginAdminUserAction;

	public ServerUnderMaintenanceResource(ManageServerStatusAction manageServerStatusAction, LoginAdminUserAction loginAdminUserAction) {
		this.manageServerStatusAction = manageServerStatusAction;
		this.loginAdminUserAction = loginAdminUserAction;
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("underMaintenance")
	public Response setServerUnderMaintenance(@FormParam("adminLoginId") String adminLoginId, @FormParam("adminPassword") String adminPassword) {
		if (!checkRequesterRole(adminLoginId, adminPassword))
			return Response.status(Response.Status.UNAUTHORIZED).build();

		manageServerStatusAction.setServerUnderMaintenance();

		return Response.ok(new ServerUnderMaintenanceResponse(manageServerStatusAction.isServerUnderMaintenance())).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("available")
	public Response setServerAvailable(@FormParam("adminLoginId") String adminLoginId, @FormParam("adminPassword") String adminPassword) {
		if (!checkRequesterRole(adminLoginId, adminPassword))
			return Response.status(Response.Status.UNAUTHORIZED).build();

		manageServerStatusAction.setServerAvailable();

		return Response.ok(new ServerUnderMaintenanceResponse(manageServerStatusAction.isServerUnderMaintenance())).build();
	}

	private boolean checkRequesterRole(@FormParam("adminLoginId") String adminLoginId, @FormParam("adminPassword") String adminPassword) {
		try {
			loginAdminUserAction.login(adminLoginId, adminPassword);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
