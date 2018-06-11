package com.etermax.spacehorse.core.authenticator.resource;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.authenticator.model.UserPrincipal;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class SpaceHorseSecurityContext implements SecurityContext {

	public static final String CUSTOM_TOKEN = "CUSTOM_TOKEN";
	private final UserPrincipal principal;

	private final SecurityContext securityContext;

	public SpaceHorseSecurityContext(UserPrincipal principal, SecurityContext securityContext) {
		this.principal = principal;
		this.securityContext = securityContext;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		Role userRole = principal.getRole();

		//Tester implies player
		if (userRole.equals(Role.TESTER)) {
			if (Role.PLAYER.toString().equals(role)) {
				return true;
			}
		}

		return userRole.toString().equals(role);
	}

	@Override
	public boolean isSecure() {
		return securityContext.isSecure();
	}

	@Override
	public String getAuthenticationScheme() {
		return CUSTOM_TOKEN;
	}

}
