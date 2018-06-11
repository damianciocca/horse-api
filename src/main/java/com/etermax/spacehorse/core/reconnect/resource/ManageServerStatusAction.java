package com.etermax.spacehorse.core.reconnect.resource;

import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;

public class ManageServerStatusAction {
	private ServerSettingsRepository serverSettingsRepository;

	public ManageServerStatusAction(ServerSettingsRepository serverSettingsRepository) {
		this.serverSettingsRepository = serverSettingsRepository;
	}

	public void setServerUnderMaintenance() {
		serverSettingsRepository.setIsServerUnderMaintenance(true);
	}

	public void setServerAvailable() {
		serverSettingsRepository.setIsServerUnderMaintenance(false);
	}

	public boolean isServerUnderMaintenance() {
		return serverSettingsRepository.getServerSettings().isUnderMaintenance();
	}
}
