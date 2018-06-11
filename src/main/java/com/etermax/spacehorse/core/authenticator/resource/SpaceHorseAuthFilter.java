package com.etermax.spacehorse.core.authenticator.resource;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.etermax.spacehorse.core.authenticator.model.Credentials;
import com.etermax.spacehorse.core.authenticator.model.UserPrincipal;
import org.assertj.core.util.Preconditions;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class SpaceHorseAuthFilter extends AuthFilter {

	public static final String LOGIN_ID = "Login-Id";
	public static final String SESSION_TOKEN = "Session-Token";
	private UserAuthenticator userAuthenticator;

	public SpaceHorseAuthFilter(UserAuthenticator userAuthenticator) {
		this.userAuthenticator = userAuthenticator;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Credentials credentials = createCredentialsFromHeaders(requestContext);
		Optional<UserPrincipal> authenticated;
		try {
			authenticated = userAuthenticator.authenticate(credentials);
		} catch (AuthenticationException e) {
			throw new WebApplicationException(e.getMessage(), Response.Status.UNAUTHORIZED);
		}

		if (authenticated.isPresent()) {
			UserPrincipal principal = authenticated.get();
			SecurityContext securityContext = new SpaceHorseSecurityContext(principal, requestContext.getSecurityContext());
			requestContext.setSecurityContext(securityContext);
		} else {
			throw new WebApplicationException("Credentials not valid", Response.Status.UNAUTHORIZED);
		}

	}

	private Credentials createCredentialsFromHeaders(ContainerRequestContext requestContext) {
		try {
			String loginId = requestContext.getHeaderString(LOGIN_ID);
			String sessionToken = requestContext.getHeaderString(SESSION_TOKEN);
			Preconditions.checkNotNullOrEmpty(loginId);
			Preconditions.checkNotNullOrEmpty(sessionToken);
			return new Credentials(loginId, sessionToken);
		} catch (Exception e) {
			throw new WebApplicationException("Unable to parse credentials", Response.Status.UNAUTHORIZED);
		}
	}

}
