package com.etermax.spacehorse.core.catalog.repository;

import java.util.Calendar;

import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class ServerSettingsRepository {

	private final int CACHE_DURATION_IN_SECONDS = 30;

	private DynamoDao dynamoDao;

	public ServerSettingsRepository(DynamoDao dao) {
		this.dynamoDao = dao;
	}

	private volatile DynamoServerSettings cachedValue;
	private volatile Calendar cachedValueExpiration;

	public void setActiveCatalogId(String catalogId) {

		DynamoServerSettings dynamoServerSettings = getServerSettings();

		dynamoServerSettings.setCatalogId(catalogId);

		dynamoDao.update(dynamoServerSettings);

		resetCache();
	}

	public void setIsServerUnderMaintenance(boolean isServerUnderMaintenance) {

		DynamoServerSettings dynamoServerSettings = getServerSettings();

		dynamoServerSettings.setUnderMaintenance(isServerUnderMaintenance);

		dynamoDao.update(dynamoServerSettings);

		resetCache();
	}

	public DynamoServerSettings getServerSettings() {

		DynamoServerSettings dynamoServerSettings = getFromCache();

		if (dynamoServerSettings == null) {
			dynamoServerSettings = (DynamoServerSettings) dynamoDao.find(new DynamoServerSettings());
			setInCache(dynamoServerSettings);
		}

		if (dynamoServerSettings == null) {
			dynamoServerSettings = new DynamoServerSettings();
		}

		return dynamoServerSettings;
	}

	private synchronized DynamoServerSettings getFromCache() {
		Calendar now = Calendar.getInstance();
		if (cachedValue == null || cachedValueExpiration == null || now.after(cachedValueExpiration)) {
			return null;
		}
		return cachedValue;
	}

	private synchronized void setInCache(DynamoServerSettings settings) {
		cachedValue = settings;

		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, CACHE_DURATION_IN_SECONDS);

		cachedValueExpiration = now;
	}

	private synchronized void resetCache() {
		cachedValue = null;
		cachedValueExpiration = null;
	}

}
