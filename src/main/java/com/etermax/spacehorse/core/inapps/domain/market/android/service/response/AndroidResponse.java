package com.etermax.spacehorse.core.inapps.domain.market.android.service.response;

import java.util.Optional;

public class AndroidResponse {

    private String productId;

    public AndroidResponse(String productId) {
        this.productId = productId;
    }

    public Optional<String> getProductId() {
        return Optional.ofNullable(productId);
    }

}
