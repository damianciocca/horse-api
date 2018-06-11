package com.etermax.spacehorse.core.shop.model;

import static com.etermax.spacehorse.core.shop.action.ShopInAppValidator.getPlatformOrFail;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.inapps.resource.response.ProductPurchaseStatus;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.user.model.User;
import com.google.common.collect.Lists;

public abstract class ApplyPendingRewardsTemplate {

	private static final Logger logger = LoggerFactory.getLogger(ApplyPendingRewardsTemplate.class);

	private InAppReceiptCreationDomainService inAppReceiptCreationDomainService;

	public ApplyPendingRewardsTemplate(InAppReceiptCreationDomainService inAppReceiptCreationDomainService) {
		this.inAppReceiptCreationDomainService = inAppReceiptCreationDomainService;
	}

	public List<InAppsItemResponse> applyPendingReceipts(Player player, User user, Catalog catalog, List<Object> receipts) {
		try {
			List<InAppsItemResponse> inAppsItemResponse = new ArrayList<>();
			String market = user.getPlatform().toString();
			MarketType marketType = MarketType.getMarketType(user.getPlatform());

			getPlatformOrFail(market);

			for (Object receipt : receipts) {

				try {
					List<InAppPurchaseReceipt> inAppPurchaseReceipts = inAppReceiptCreationDomainService
							.createReceipts(user, mapInAppDefinitions(catalog), receipt);

					List<RewardResponse> rewards = new ArrayList<>();

					for (InAppPurchaseReceipt inAppPurchaseReceipt : inAppPurchaseReceipts) {

						try {
							rewards.addAll(applyPendingRewards(player, marketType, catalog, inAppPurchaseReceipt.getReceipt().getProductId()));
							inAppReceiptCreationDomainService.persistReceipt(inAppPurchaseReceipt);
							inAppsItemResponse
									.add(new InAppsItemResponse(new ProductPurchaseStatus(inAppPurchaseReceipt.getReceipt().getProductId(), true),
											rewards));
						} catch (Exception ex) {
							logger.error("Exception trying to apply pending receipt", ex);
						}
					}

				} catch (Exception ex) {
					logger.error("Exception trying to apply pending receipt", ex);
				}
			}
			return inAppsItemResponse;

		} catch (Exception e) {
			logger.error(format("An exception occurs when try to apply pending rewards for user [ %s ] ", user), e);
			return Lists.newArrayList();
		}
	}

	protected abstract List<InAppDefinition> mapInAppDefinitions(Catalog catalog);

	protected abstract List<RewardResponse> applyPendingRewards(Player player, MarketType marketType, Catalog catalog, String productId);
}


