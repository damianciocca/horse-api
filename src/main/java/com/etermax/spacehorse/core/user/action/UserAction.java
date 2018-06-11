package com.etermax.spacehorse.core.user.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.user.exceptions.RoleChangingNotAllowedException;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class UserAction {

	private static final Logger logger = LoggerFactory.getLogger(UserAction.class);

	private static final String ADMIN = "admin";
	private static final String SUPPORT = "support";
	private UserRepository userRepository;

	public UserAction(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findByUserId(String userId) {
		return userRepository.find(userId);
	}

	public User find(String loginId) {
		return userRepository.find(loginId);
	}

	public User updateRole(String userId, Role role) {
		User user = this.findByUserId(userId);
		if (userNotFound(user)) {
			logger.error("User not found!");
			throw new UserNotFoundException("User not found!");
		}
		if (isDefaultAdminUser(user) || isDefaultSupportUser(user)) {
			logger.error("Cannot change role for default admin user or default support user!");
			throw new RoleChangingNotAllowedException("Cannot change role for default admin user or default support user!");
		}
		if (roleToChangeIsValid(role)) {
			logger.error("Cannot change role to ADMIN or SUPPORT. Roles not allowed");
			throw new RoleChangingNotAllowedException("Cannot change role to ADMIN or SUPPORT. Roles not allowed");
		}
		user.setRole(role);
		userRepository.update(user);
		return user;
	}

	private boolean roleToChangeIsValid(Role role) {
		return isAdmin(role) || isSupport(role);
	}

	private boolean isSupport(Role role) {
		return Role.SUPPORT.equals(role);
	}

	private boolean isAdmin(Role role) {
		return Role.ADMIN.equals(role);
	}

	private boolean userNotFound(User user) {
		return user == null;
	}

	public User createUser(String userId, String password, Role role, Platform platform) {
		User user = new User(userId, password, role, platform);
		userRepository.add(user);
		return user;
	}

	public void saveNewPassForUser(String userId, String newPassword) {
		User user = userRepository.find(userId);
		user.addPassword(newPassword);
		userRepository.update(user);
	}

	private boolean isDefaultAdminUser(User user) {
		return ADMIN.equals(user.getUserId());
	}

	private boolean isDefaultSupportUser(User user) {
		return SUPPORT.equals(user.getUserId());
	}
}
