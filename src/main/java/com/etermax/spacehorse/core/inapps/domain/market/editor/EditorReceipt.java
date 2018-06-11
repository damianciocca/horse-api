package com.etermax.spacehorse.core.inapps.domain.market.editor;

import com.etermax.spacehorse.core.inapps.domain.Receipt;

public class EditorReceipt extends Receipt {

	private String productId;

	@Override
	public String getProductId() {
		return productId;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public EditorReceipt(Object receipt) {
		if (receipt instanceof String)
			productId = (String) receipt;
	}
}
