package com.etermax.spacehorse.core.login.action;

import java.util.UUID;

import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class LoginSupportUserAction {

	private final UserRepository userRepository;

	public LoginSupportUserAction(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void login(String loginId, String password) {
		User user = userRepository.find(loginId);
		if (userNotExists(user) || userIsNotSupport(user) || passwordIsNotValid(password, user)) {
			throw new ApiException("User is not support");
		}
		updateSessionToken(user);
	}

	private boolean passwordIsNotValid(String password, User user) {
		return !user.validatePassword(password);
	}

	private boolean userIsNotSupport(User user) {
		return !user.isSupport();
	}

	private boolean userNotExists(User user) {
		return user == null;
	}

	private void updateSessionToken(User user) {
		user.setSessionToken(UUID.randomUUID().toString());
		userRepository.update(user);
	}
}
