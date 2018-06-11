package com.etermax.spacehorse.core.user.model;

import com.etermax.spacehorse.core.authenticator.model.Role;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

	private User user;

	private String userId;

	private String password;

	private Role role;

	private Platform platform;

	@Before
	public void setUp() {
		userId = "Login";
		password = "Password";
		role = Role.ADMIN;
		platform = Platform.ANDROID;
		user = new User(userId, password, role, platform);
	}

	@Test
	public void testNewUser() {
		assertNotNull(user);
		assertEquals(userId, user.getUserId());
		assertNotNull(user.getPassword());
		assertNotNull(user.getRole());
		assertNotNull(user.getSessionToken());
	}

	@Test
	public void testPassword() {
		assertNotSame(password, user.getPassword());
		assertTrue(user.validatePassword(password));
	}

	@Test
	public void testRole() {
		assertEquals(role, user.getRole());
		assertFalse(user.isTester());
		assertTrue(user.isAdmin());
		assertTrue(user.isAdminOrTester());
	}

}
