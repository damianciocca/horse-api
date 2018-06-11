package com.etermax.spacehorse.core.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.etermax.spacehorse.core.admin.resource.AdminResource;
import com.etermax.spacehorse.core.admin.resource.ChangeRoleRequest;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.login.action.LoginAdminUserAction;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.exceptions.RoleChangingNotAllowedException;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.mock.MockUtils;

public class AdminResourceTest {

	private static final String LOGIN_ID = "userId";
	private static final Role ROLE_TESTER = Role.TESTER;

	@Test
	public void testChangeUserRoleWhenRequesterIsAdminAndHasValidCredentials() {
		// given
		Platform androidPlatform = Platform.ANDROID;
		UserAction userAction = MockUtils.mockUserAction("userId", androidPlatform);
		User updatedUser = mock(User.class);
		when(userAction.updateRole(anyString(), anyObject())).thenReturn(updatedUser);
		LoginAdminUserAction loginAction = aLoginUserAction(androidPlatform, Role.ADMIN);

		String adminLoginId = "adminLoginId";
		String adminPassword = "adminPassword";

		// when
		Response response = new AdminResource(userAction, loginAction).changeUserRole(aChangeRoleRequest(adminLoginId, adminPassword, ROLE_TESTER));

		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(response.getEntity()).isEqualTo("Updated User " + LOGIN_ID + " with role " + ROLE_TESTER);
	}

	@Test
	public void testChangeUserRoleReturnsBadRequestWhenUserDoesntExist() {
		// given
		Platform androidPlatform = Platform.ANDROID;
		UserAction userAction = MockUtils.mockUserAction("userId", androidPlatform);
		when(userAction.updateRole(anyString(), anyObject())).thenThrow(UserNotFoundException.class);

		LoginAdminUserAction loginAction = aLoginUserAction(androidPlatform, Role.ADMIN);

		String adminLoginId = "adminLoginId";
		String adminPassword = "adminPassword";

		// when
		Response response = new AdminResource(userAction, loginAction).changeUserRole(aChangeRoleRequest(adminLoginId, adminPassword, ROLE_TESTER));

		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	public void testChangeUserRoleReturnsUnauthorizedWhenRoleIsNotAdmin() {
		// given
		Platform androidPlatform = Platform.ANDROID;
		UserAction userAction = MockUtils.mockUserAction("userId", androidPlatform);
		when(userAction.updateRole(anyString(), anyObject())).thenReturn(null);

		LoginAdminUserAction loginAction = aLoginUserActionThrowException(androidPlatform, Role.TESTER);

		String adminLoginId = "adminLoginId";
		String adminPassword = "adminPassword";

		// when
		Response response = new AdminResource(userAction, loginAction).changeUserRole(aChangeRoleRequest(adminLoginId, adminPassword, ROLE_TESTER));

		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.UNAUTHORIZED);
	}

	@Test
	public void testChangeUserRoleReturnsBadRequestWhenUserIsADefaultAdminOrDefaultSupport() {
		// given
		Platform androidPlatform = Platform.ANDROID;
		UserAction userAction = MockUtils.mockUserAction("userId", androidPlatform);
		when(userAction.updateRole(anyString(), anyObject())).thenThrow(RoleChangingNotAllowedException.class);

		LoginAdminUserAction loginAction = aLoginUserAction(androidPlatform, Role.ADMIN);

		String adminLoginId = "adminLoginId";
		String adminPassword = "adminPassword";

		// when
		Response response = new AdminResource(userAction, loginAction).changeUserRole(aChangeRoleRequest(adminLoginId, adminPassword, ROLE_TESTER));

		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
	}

	private ChangeRoleRequest aChangeRoleRequest(String adminLoginId, String adminPassword, Role role) {
		return new ChangeRoleRequest(adminLoginId, adminPassword, LOGIN_ID, role);
	}

	private LoginAdminUserAction aLoginUserAction(Platform androidPlatform, Role adminRole) {
		String password = "password";
		String userId = "userId";
		User user = new User(userId, password, adminRole, androidPlatform);
		LoginAdminUserAction loginAction = mock(LoginAdminUserAction.class);
		when(loginAction.login(anyString(), anyString())).thenReturn(user);
		return loginAction;
	}

	private LoginAdminUserAction aLoginUserActionThrowException(Platform androidPlatform, Role adminRole) {
		String password = "password";
		String userId = "userId";
		User user = new User(userId, password, adminRole, androidPlatform);
		LoginAdminUserAction loginAction = mock(LoginAdminUserAction.class);
		when(loginAction.login(anyString(), anyString())).thenThrow(ApiException.class);
		return loginAction;
	}
}
