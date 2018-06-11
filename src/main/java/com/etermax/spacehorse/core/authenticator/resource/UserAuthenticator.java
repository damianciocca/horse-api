package com.etermax.spacehorse.core.authenticator.resource;

import java.util.Optional;

import com.etermax.spacehorse.core.authenticator.model.UserPrincipal;
import com.etermax.spacehorse.core.authenticator.model.Credentials;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.User;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class UserAuthenticator implements Authenticator<Credentials, UserPrincipal> {

	private final UserAction userAction;

	public UserAuthenticator(UserAction userAction) {
		this.userAction = userAction;
	}

	@Override
	public Optional<UserPrincipal> authenticate(Credentials credentials) throws AuthenticationException {
		User user = validateCredentials(credentials);
		return Optional.of(new UserPrincipal(user.getUserId(), user.getRole()));
	}

	private User validateCredentials(Credentials credentials) {
		String loginId = credentials.getLoginId();
		User user = userAction.findByUserId(loginId);
		if(user == null || !user.getSessionToken().equals(credentials.getSessionToken())) {
			throw new InvalidCredentialsException("Invalid Credentials Exception");
		}
		return user;
	}

}
