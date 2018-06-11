package com.etermax.spacehorse.core.shop.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.Markets;
import com.etermax.spacehorse.core.inapps.domain.market.ReceiptFactory;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.IosReceipt;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.inapps.resource.response.ProductPurchaseStatus;
import com.etermax.spacehorse.core.shop.action.ShopInAppValidator;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;

public class InAppReceiptCreationDomainService {

	private static final Logger logger = LoggerFactory.getLogger(InAppReceiptCreationDomainService.class);

	private final ShopInAppValidator shopInAppValidator;
	private final EnviromentType enviromentType;
	private final Markets markets;
	private final MarketRepository marketRepository;

	public InAppReceiptCreationDomainService(Markets markets, MarketRepository marketRepository, EnviromentType enviromentType) {
		this.enviromentType = enviromentType;
		this.shopInAppValidator = new ShopInAppValidator();
		this.markets = markets;
		this.marketRepository = marketRepository;
	}

	public InAppPurchaseReceipt createReceipt(User user, List<InAppDefinition> inAppDefinitions, String itemId, Object rawReceipt) {
		String market = user.getPlatform().toString();
		Platform platform = Platform.valueOf(market.toUpperCase());
		logger.info("The raw receipt is: {}", rawReceipt);

		if (!canMakeInAppPurchases(platform)) {
			throw new ApiException("Purhcases not enabled in current environment for platform " + platform);
		}

		validateInAppItemId(inAppDefinitions, itemId);

		List<Receipt> receipts = ReceiptFactory.createReceipts(platform, rawReceipt);
		Receipt matchingReceipt = null;

		for (Receipt receipt : receipts) {

			try {
				updateReceiptIsValid(platform, receipt);

				shopInAppValidator
						.validateInAppPurchase(user, inAppDefinitions, itemId, new ProductPurchaseStatus(receipt.getProductId(), receipt.isValid()));

				shopInAppValidator.validateReceipt(receipt, marketRepository, platform);

				matchingReceipt = receipt;

			} catch (Exception ex) {
				logger.error("Receipt validation error", ex);
			}
		}

		if (matchingReceipt == null) {
			throw new ApiException("No valid receipt could be created from the received receipt");
		}

		return new InAppPurchaseReceipt(platform, matchingReceipt, user.getUserId());
	}

	public List<InAppPurchaseReceipt> createReceipts(User user, List<InAppDefinition> inAppDefinitions, Object rawReceipt) {
		String market = user.getPlatform().toString();
		Platform platform = Platform.valueOf(market.toUpperCase());
		MarketType marketType = MarketType.getMarketType(user.getPlatform());

		if (!canMakeInAppPurchases(platform)) {
			throw new ApiException("Purhcases not enabled in current environment for platform " + platform);
		}

		List<Receipt> receipts = ReceiptFactory.createReceipts(platform, rawReceipt);
		List<InAppPurchaseReceipt> purchaseReceipts = new ArrayList<>();

		for (Receipt receipt : receipts) {

			String itemId = inAppDefinitions.stream()
					.filter(inapp -> inapp.getProductId().equals(receipt.getProductId()) && inapp.getMarketType().equals(marketType)).findFirst()
					.map(inApp -> inApp.getItemId()).orElse("");

			if (!itemId.isEmpty()) {

				try {
					InAppPurchaseReceipt purchaseReceipt = createReceipt(user, inAppDefinitions, itemId, rawReceipt);
					purchaseReceipts.add(purchaseReceipt);
				} catch (Exception ex) {
					//Any error is already logged by createReceipt() call
				}
			}
		}

		return purchaseReceipts;
	}

	private void validateInAppItemId(List<InAppDefinition> inAppDefinitions, String itemId) {
		inAppDefinitions.stream().filter(inAppDef -> inAppDef.getItemId().equals(itemId)).findFirst()
				.orElseThrow(() -> new ApiException("Invalid in app item id [ " + itemId + " ]. Actual in app items [ " + inAppDefinitions + " ] "));
	}

	public void persistReceipt(InAppPurchaseReceipt inAppPurchaseReceipt) {
		Platform platform = inAppPurchaseReceipt.getPlatform();
		Receipt receipt = inAppPurchaseReceipt.getReceipt();
		String userId = inAppPurchaseReceipt.getUserId();

		if (platform.equals(Platform.IOS)) {
			this.marketRepository.addIosReceipt((IosReceipt) receipt, userId);
			logger.info("Receipt from user: {} saved successfully: {}", userId, receipt);
		} else if (platform.equals(Platform.ANDROID)) {
			this.marketRepository.addAndroidReceipt((AndroidReceipt) receipt, userId);
			logger.info("Receipt from user: {} saved successfully: {}", userId, ((AndroidReceipt) receipt).getTransactionId());
		} else if (platform.equals(Platform.EDITOR)) {
		} else {
			logger.error("Receipt's platform doesn't belong to any available market: " + platform.toString());
			throw new ApiException("Market not found.");
		}
	}

	private boolean canMakeInAppPurchases(Platform platform) {
		return markets.findById(platform).map(x -> x.canBeUsedIn(enviromentType)).orElseThrow(() -> new ApiException("Market not found."));
	}

	private Receipt updateReceiptIsValid(Platform platform, Receipt receipt) {
		return markets.findById(platform).map(market -> {
			receipt.setValid(market.validate(receipt));
			return receipt;
		}).orElseThrow(() -> new ApiException("Market not found."));
	}
}
