package com.etermax.spacehorse.core.user.repository;

import com.etermax.spacehorse.core.user.model.User;

public interface UserRepository {

	void update(User user);
	void add(User user);
	User find(String loginId);

}
