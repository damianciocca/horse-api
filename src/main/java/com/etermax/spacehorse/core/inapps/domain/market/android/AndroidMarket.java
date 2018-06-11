package com.etermax.spacehorse.core.inapps.domain.market.android;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidService;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.response.AndroidResponse;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidMarket implements Market {

    private AndroidService androidService;

    private static final Logger logger = LoggerFactory.getLogger(AndroidMarket.class);

    public AndroidMarket(AndroidService androidService) {
        this.androidService = androidService;
    }

	@Override
	public boolean canBeUsedIn(EnviromentType enviromentType) {
		return true;
	}

	@Override
    public boolean validate(Receipt receipt) {
        try {
			AndroidReceipt androidReceipt = (AndroidReceipt) receipt;
			AndroidResponse androidResponse = verifyData(androidReceipt);
			Validate.notNull(androidResponse);
			Validate.notNull(androidResponse.getProductId());
			return true;
        } catch (Exception e) {
            logger.error("Error validating receipt: {}. With exception: {}", receipt, e);
			return false;
        }
    }

    private AndroidResponse verifyData(AndroidReceipt androidReceipt) {
        try {
            return androidService.verifyReceipt(androidReceipt);
        }catch (Exception ex){
            logger.error("Error verifying receipt: {}. With exception: {}", androidReceipt, ex);
            throw InAppsErrors.invalidReceipt();
        }
    }

}
