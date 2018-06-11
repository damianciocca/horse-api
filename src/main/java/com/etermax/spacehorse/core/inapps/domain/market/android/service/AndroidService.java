package com.etermax.spacehorse.core.inapps.domain.market.android.service;

import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.response.AndroidResponse;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.SignatureVerifier;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidService {

    private static final Logger logger = LoggerFactory.getLogger(AndroidService.class);

	private AndroidProperties androidProperties;

    private SignatureVerifier verifier;

    public AndroidService(AndroidProperties androidProperties, SignatureVerifier verifier) {
        this.verifier = verifier;
		this.androidProperties = androidProperties;
		if (verifier == null || androidProperties == null || androidProperties.getPackageName() == null || androidProperties.getSignature() == null) {
            logger.info("Invalid android service configuration");
			InAppsErrors.invalidConfiguration();
		}
    }

	public AndroidResponse verifyReceipt(AndroidReceipt androidReceipt) {
    	if (androidReceipt.getProductId() == null || androidReceipt.getProductId().isEmpty()) {
            logger.info("Product Id is null or empty " + androidReceipt);
			throw InAppsErrors.invalidReceipt();
		}
		if (androidReceipt.getPackageName() == null || !androidReceipt.getPackageName().equals(androidProperties.getPackageName())) {
            logger.info("Invalid package name " + androidReceipt.getPackageName());
    	    throw InAppsErrors.invalidReceipt();
		}
        if(!verifier.verifySignature(androidReceipt.getSignature(), androidReceipt.getPayload().getData().toString())) {
            logger.info("Invalid signature verifier");
            logger.info(androidReceipt.getSignature());
            logger.info(androidReceipt.getDataAsString());
            throw InAppsErrors.invalidReceipt();
        }
        return new AndroidResponse(androidReceipt.getProductId());
    }

}
