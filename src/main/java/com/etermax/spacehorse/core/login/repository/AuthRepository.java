package com.etermax.spacehorse.core.login.repository;

import com.etermax.spacehorse.core.user.model.Platform;

public interface AuthRepository {

	String link(Platform platform, String id, String userId);

	String findById(Platform platform, String id);

	String findByUserId(Platform platform, String userId);

	void delete(Platform platform, String userId);
}
