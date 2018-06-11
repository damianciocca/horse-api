package com.etermax.spacehorse.core.user.repository.dynamo;

import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class UserDynamoRepository implements UserRepository {

	private DynamoDao dynamoDao;

	public UserDynamoRepository(DynamoDao dao) {
		this.dynamoDao = dao;
	}

	public void update(User user) {
		DynamoUser dynamoUser = DynamoUser.createFromUser(user);
		dynamoDao.update(dynamoUser);
	}

	public void add(User user) {
		DynamoUser dynamoUser = DynamoUser.createFromUser(user);
		dynamoDao.add(dynamoUser);
	}

	public User find(String userId) {
		DynamoUser dynamoUser = newDynamoUserWithId(userId);
		DynamoUser found;
        try {
            found = (DynamoUser) dynamoDao.find(dynamoUser);
        } catch (Throwable e) {
            return null;
        }
        if (found == null) {
            return null;
        }
        return DynamoUser.mapFromDynamoUserToUser(found);

	}

	private DynamoUser newDynamoUserWithId(String id) {
		DynamoUser dynamoUser = new DynamoUser();
		dynamoUser.setId(id);
		return dynamoUser;
	}

}
