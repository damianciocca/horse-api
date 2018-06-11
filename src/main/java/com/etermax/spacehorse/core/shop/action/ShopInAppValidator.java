package com.etermax.spacehorse.core.shop.action;

import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.error.DuplicatedPurchaseReceiptException;
import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.inapps.resource.response.ProductPurchaseStatus;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ShopInAppValidator {
    private static final Logger logger = LoggerFactory.getLogger(ShopInAppValidator.class);

    public ShopInAppValidator() {
    }

    public static Platform getPlatformOrFail(String market) {
        try {
            return Platform.valueOf(market.toUpperCase());
        } catch (Exception e) {
            throw  new ApiException("Platform not found.");
        }
    }

    public void validateInAppPurchase(User user, List<InAppDefinition> inAppDefinitions, String itemId,
            ProductPurchaseStatus productPurchaseStatus) {
        validateProductPurchaseStatus(productPurchaseStatus);
        validateReceiptMatchWithPurchasedItem(inAppDefinitions, itemId, productPurchaseStatus.getProductId(), MarketType.valueOf(user.getPlatform().toString()));
    }

    private void validateProductPurchaseStatus(ProductPurchaseStatus productPurchaseStatus) {
        if (!productPurchaseStatus.isValid()) {
            logger.error("Error validating receipt: Receipt validation returned false");
            throw new ApiException("Error validating receipt");
        }
    }

    private void validateReceiptMatchWithPurchasedItem(List<InAppDefinition> inAppDefinitions, String itemId, String productId, MarketType marketType) {
        Optional<InAppDefinition> marketProduct = inAppDefinitions.stream().filter(byProductIdAndMarketType(productId, marketType)).findFirst();

        if (!marketProduct.isPresent()) {
            logger.error("No in-app entry with market " + marketType + " and productId " + productId + " found in in-apps catalog");
            throw new ApiException("Product not found in market");
        }

        if (!marketProduct.get().getItemId().equals(itemId)) {
            logger.error("In-app entry with market " + marketType + " and productId " + productId + " found in in-apps catalog doesn't match the "
                    + "receive itemId " + itemId);
            throw new ApiException("ShopItemId doesn't match the product / market");
        }
    }

    private Predicate<InAppDefinition> byProductIdAndMarketType(String productId, MarketType marketType) {
        return inAppDefinition -> inAppDefinition.getProductId().equalsIgnoreCase(productId) && inAppDefinition.getMarketType() == marketType;
    }

    public void validateReceipt(Receipt receiptToSend, MarketRepository marketRepository, Platform platform) {
        if(!receiptToSend.isValid()) {
            throw new ApiException("Receipt is not valid");
        }
        if(marketRepository.isDuplicated(receiptToSend, platform)) {
            throw new DuplicatedPurchaseReceiptException();
        }
    }
}
