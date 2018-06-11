package com.etermax.spacehorse.core.inapps.domain.market.ios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.service.IosService;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;

public class IosMarket implements Market {

	private static final Logger logger = LoggerFactory.getLogger(IosMarket.class);

	private IosService iosService;

	public IosMarket(IosService iosService) {
		this.iosService = iosService;
	}

	@Override
	public boolean canBeUsedIn(EnviromentType enviromentType) {
		return true;
	}

	@Override
	public boolean validate(Receipt receipt) {
		try {
			IosReceipt iosReceipt = (IosReceipt) receipt;
			return verifyData(iosReceipt);
		} catch (Exception e) {
			logger.error("Error verifying receipt: {}. With exception: {}", receipt, e);
			return false;
		}
	}

	private boolean verifyData(IosReceipt iosReceipt) {
		try {
			return iosService.verifyReceipt(iosReceipt);
		} catch (Exception ex) {
			logger.error("Error verifying receipt: {}. With exception: {}", iosReceipt, ex);
			throw InAppsErrors.invalidReceipt();
		}
	}

}
