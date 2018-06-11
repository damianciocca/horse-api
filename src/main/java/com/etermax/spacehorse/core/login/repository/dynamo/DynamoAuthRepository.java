package com.etermax.spacehorse.core.login.repository.dynamo;

import java.util.Optional;

import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoAuthRepository implements AuthRepository {

	private final DynamoDao<DynamoGameCenterAuthUser> gameCenterDynamoDao;
	private final DynamoDao<DynamoGooglePlayAuthUser> googlePlayDynamoDao;

	public DynamoAuthRepository(DynamoDao dynamoDao) {
		this.gameCenterDynamoDao = dynamoDao;
		this.googlePlayDynamoDao = dynamoDao;
	}

	@Override
	public String link(Platform platform, String id, String userId) {
		switch (platform) {
			case ANDROID:
				return linkGooglePlayServices(id, userId);
			case IOS:
				return linkGameCenter(id, userId);
		}

		throw new ApiException("Linking with platform " + platform + " not implemented");
	}

	@Override
	public String findById(Platform platform, String id) {
		switch (platform) {
			case ANDROID:
				return findByGooglePlayId(id);
			case IOS:
				return findByGameCenterId(id);
		}

		throw new ApiException("Linking with platform " + platform + " not implemented");
	}

	@Override
	public String findByUserId(Platform platform, String userId) {
		switch (platform) {
			case ANDROID:
				return findInGooglePlayByUserId(userId);
			case IOS:
				return findInGameCenterByUserId(userId);
		}

		throw new ApiException("Linking with platform " + platform + " not implemented");
	}

	@Override
	public void delete(Platform platform, String userId) {
		switch (platform) {
			case ANDROID:
				googlePlayDynamoDao.remove (new DynamoGooglePlayAuthUser(userId));
				break;
			case IOS:
				gameCenterDynamoDao.remove (new DynamoGameCenterAuthUser(userId));
				break;
		}
	}

	private String linkGameCenter(String id, String gameCenterId) {
		Optional<DynamoGameCenterAuthUser> dynamoAuthUser = gameCenterDynamoDao.findByGameCenterUserId(gameCenterId);
		if (!dynamoAuthUser.isPresent()) {
			DynamoGameCenterAuthUser dynamoAuthUserToAdd = newDynamoGameCenterAuthUserWithId(id);
			dynamoAuthUserToAdd.setGameCenterId(gameCenterId);
			gameCenterDynamoDao.add(dynamoAuthUserToAdd);
		} else {
			id = dynamoAuthUser.get().getId();
		}
		return id;
	}

	private DynamoGameCenterAuthUser newDynamoGameCenterAuthUserWithId(String id) {
		DynamoGameCenterAuthUser dynamoAuthUser = new DynamoGameCenterAuthUser(id);
		dynamoAuthUser.setId(id);
		return dynamoAuthUser;
	}

	private String findByGameCenterId(String gameCenterId) {
		Optional<DynamoGameCenterAuthUser> dynamoAuthUser = gameCenterDynamoDao.findByGameCenterUserId(gameCenterId);
		return dynamoAuthUser.map(DynamoGameCenterAuthUser::getUserId).orElse(null);
	}

	private String findInGameCenterByUserId(String userId) {
		DynamoGameCenterAuthUser authUser = gameCenterDynamoDao.find(DynamoGameCenterAuthUser.class, userId);
		return authUser != null ? authUser.getGameCenterId() : "";
	}

	private String linkGooglePlayServices(String id, String googlePlayUserId) {
		Optional<DynamoGooglePlayAuthUser> dynamoAuthUser = googlePlayDynamoDao.findByGooglePlayUserId(googlePlayUserId);
		if (!dynamoAuthUser.isPresent()) {
			DynamoGooglePlayAuthUser dynamoAuthUserToAdd = newDynamoGooglePlayAuthUserWithId(id);
			dynamoAuthUserToAdd.setGooglePlayId(googlePlayUserId);
			googlePlayDynamoDao.add(dynamoAuthUserToAdd);
		} else {
			id = dynamoAuthUser.get().getId();
		}
		return id;
	}

	private DynamoGooglePlayAuthUser newDynamoGooglePlayAuthUserWithId(String id) {
		DynamoGooglePlayAuthUser dynamoAuthUser = new DynamoGooglePlayAuthUser(id);
		dynamoAuthUser.setId(id);
		return dynamoAuthUser;
	}

	private String findByGooglePlayId(String googlePlayId) {
		Optional<DynamoGooglePlayAuthUser> dynamoAuthUser = googlePlayDynamoDao.findByGooglePlayUserId(googlePlayId);
		return dynamoAuthUser.map(DynamoGooglePlayAuthUser::getUserId).orElse(null);
	}

	private String findInGooglePlayByUserId(String userId) {
		DynamoGooglePlayAuthUser authUser = googlePlayDynamoDao.find(DynamoGooglePlayAuthUser.class, userId);
		return authUser != null ? authUser.getGooglePlayId() : "";
	}
}
