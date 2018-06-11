package com.etermax.spacehorse.core.inapps.domain.market.ios.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.inapps.domain.market.ios.IosReceipt;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;

public class IosService {

	private static final Logger logger = LoggerFactory.getLogger(IosService.class);

	private IosProperties iosProperties;

	public IosService(IosProperties iosProperties) {
		this.iosProperties = iosProperties;

		if (iosProperties == null || iosProperties.getBundleId() == null) {
			logger.info("Invalid ios service configuration");
			InAppsErrors.invalidConfiguration();
		}
	}

	public boolean verifyReceipt(IosReceipt iosReceipt) {

		if (iosReceipt.getProductId() == null || iosReceipt.getProductId().isEmpty()) {
			logger.info("Product Id is null or empty " + iosReceipt);
			return false;
		}
		if (iosReceipt.getBundleId() == null || !iosReceipt.getBundleId().equals(iosProperties.getBundleId())) {
			logger.info("Invalid package name " + iosReceipt.getBundleId());
			return false;
		}

		return true;
	}
}
