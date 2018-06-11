package com.etermax.spacehorse.core.inapps.domain.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidMarket;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidProperties;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidService;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.PublicKeySignatureVerifier;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.SignatureVerifier;
import com.etermax.spacehorse.core.inapps.domain.market.editor.EditorMarket;
import com.etermax.spacehorse.core.inapps.domain.market.ios.IosMarket;
import com.etermax.spacehorse.core.inapps.domain.market.ios.service.IosProperties;
import com.etermax.spacehorse.core.inapps.domain.market.ios.service.IosService;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;
import com.etermax.spacehorse.core.user.model.Platform;

public class MarketsFactory {

	private static final Logger logger = LoggerFactory.getLogger(MarketsFactory.class);

	private MarketsFactory() {
	}

	public static Markets buildMarkets(AndroidProperties androidProperties, IosProperties iosProperties) {
		MemoryMarkets memoryMarkets = new MemoryMarkets();
		memoryMarkets.registerMarket(Platform.EDITOR, editorMarket());
		memoryMarkets.registerMarket(Platform.IOS, iosMarket(iosProperties));
		memoryMarkets.registerMarket(Platform.ANDROID, androidMarket(androidProperties));
		return memoryMarkets;
	}

	private static Market editorMarket() {
		return new EditorMarket();
	}

	private static Market androidMarket(AndroidProperties androidProperties) {
		return new AndroidMarket(getGoogleMarketService(androidProperties));
	}

	private static Market iosMarket(IosProperties iosProperties) {
		return new IosMarket(getIosMarketService(iosProperties));
	}

	private static IosService getIosMarketService(IosProperties iosProperties) {
		return new IosService(iosProperties);
	}

	private static AndroidService getGoogleMarketService(AndroidProperties androidProperties) {
		return new AndroidService(androidProperties, getSignatureVerifier(androidProperties));
	}

	private static SignatureVerifier getSignatureVerifier(AndroidProperties androidProperties) {
		if (androidProperties == null || androidProperties.getSignature() == null || androidProperties.getPackageName() == null) {
			logger.error("Failure while loading android properties");
			InAppsErrors.invalidConfiguration();
		}
		return new PublicKeySignatureVerifier(androidProperties.getSignature());
	}

}
