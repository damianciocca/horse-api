package com.etermax.spacehorse.core.login.action;

import java.util.UUID;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class LoginAdminUserAction {

	private final UserRepository userRepository;

	public LoginAdminUserAction(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(String loginId, String password) {
		User user = userRepository.find(loginId);
		if (userNotExists(user) || userIsNotAdmin(user) || passwordIsNotValid(password, user)) {
			throw new ApiException("User is not admin");
		}
		updateSessionToken(user);
		return user;
	}

	private boolean passwordIsNotValid(String password, User user) {
		return !user.validatePassword(password);
	}

	private boolean userIsNotAdmin(User user) {
		return !user.getRole().equals(Role.ADMIN);
	}

	private boolean userNotExists(User user) {
		return user == null;
	}

	private void updateSessionToken(User user) {
		user.setSessionToken(UUID.randomUUID().toString());
		userRepository.update(user);
	}
}
