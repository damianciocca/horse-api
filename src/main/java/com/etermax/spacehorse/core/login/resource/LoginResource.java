package com.etermax.spacehorse.core.login.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.catalog.exception.UndefinedCatalogException;
import com.etermax.spacehorse.core.error.ClientResponseException;
import com.etermax.spacehorse.core.login.action.LoginExistentUserAction;
import com.etermax.spacehorse.core.login.action.LoginInfo;
import com.etermax.spacehorse.core.login.action.LoginNewUserAction;
import com.etermax.spacehorse.core.login.error.InvalidClientException;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;
import com.etermax.spacehorse.core.login.model.ClientVersionValidator;
import com.etermax.spacehorse.core.login.resource.request.LoginRequest;
import com.etermax.spacehorse.core.login.resource.response.LoginResponse;
import com.etermax.spacehorse.core.login.resource.response.LoginResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.ServerStatusResponse;
import com.etermax.spacehorse.core.reconnect.resource.ManageServerStatusAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "/v1/login", tags = "Login")
@Path("/v1/login")
@Produces("application/json")
public class LoginResource {

	private static final Logger logger = LoggerFactory.getLogger(LoginResource.class);

	private final LoginExistentUserAction loginExistentUserAction;
	private final LoginNewUserAction loginNewUserAction;
	private final ClientVersionValidator clientVersionValidator;
	private final ManageServerStatusAction manageServerStatusAction;
	private final LoginResponseFactory loginResponseFactory;

	public LoginResource(LoginExistentUserAction loginExistentUserAction, LoginNewUserAction loginNewUserAction,
			ClientVersionValidator clientVersionValidator, ManageServerStatusAction manageServerStatusAction,
			LoginResponseFactory loginResponseFactory) {
		this.loginExistentUserAction = loginExistentUserAction;
		this.loginNewUserAction = loginNewUserAction;
		this.clientVersionValidator = clientVersionValidator;
		this.manageServerStatusAction = manageServerStatusAction;
		this.loginResponseFactory = loginResponseFactory;
	}

	@POST
	@ApiOperation("Returns login information and session-id.")
	public Response login(LoginRequest loginRequest) {
		logger.info("incoming login request. LoginId ===> " + loginRequest.getLoginId());
		String loginId = loginRequest.getLoginId();
		String password = loginRequest.getPassword();
		Integer clientVersion = loginRequest.getClientVersion();

		if (isServerUnderMaintenance()) {
			return Response.ok(buildServerUnderMaintenanceResponse()).build();
		}

		try {
			clientVersionValidator.validate(clientVersion);
			LoginInfo loginInfo;
			if (hasIdAndPassword(loginId, password)) {
				logger.info("validating user {}", loginId);
				loginInfo = loginExistentUserAction.login(loginId, password, loginRequest.getPlatform());
			} else {
				logger.info("userId not present. Creating new user");
				loginInfo = loginNewUserAction.login(loginRequest.getPlatform());
			}
			LoginResponse loginResponse = loginResponseFactory.createFrom(loginInfo);

			return Response.ok(loginResponse).build();
		} catch (InvalidClientException | UndefinedCatalogException e) {
			throw new ClientResponseException(e, Response.Status.FORBIDDEN.getStatusCode());
		} catch (InvalidCredentialsException e) {
			throw new ClientResponseException(e, Response.Status.UNAUTHORIZED.getStatusCode());
		}
	}

	private LoginResponse buildServerUnderMaintenanceResponse() {
		return new LoginResponse(new ServerStatusResponse(ServerStatusResponse.UNDER_MAINTENANCE, 0, "Server is Under Maintenance"));
	}

	private boolean hasIdAndPassword(String id, String password) {
		return (id != null && !id.isEmpty()) && (password != null && !password.isEmpty());
	}

	public boolean isServerUnderMaintenance() {
		return manageServerStatusAction.isServerUnderMaintenance();
	}
}
