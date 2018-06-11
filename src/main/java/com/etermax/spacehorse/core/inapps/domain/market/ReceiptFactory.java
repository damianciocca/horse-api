package com.etermax.spacehorse.core.inapps.domain.market;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.inapps.domain.Receipt;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.editor.EditorReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.IosReceiptFactory;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;

public final class ReceiptFactory {

	static private IosReceiptFactory iosReceiptFactory = new IosReceiptFactory();

	static public List<Receipt> createReceipts(com.etermax.spacehorse.core.user.model.Platform platform, Object receipt) {
		List<Receipt> receipts = new ArrayList<>();

		switch (platform) {
			case IOS:
				receipts.addAll(iosReceiptFactory.buildIosReceipts(receipt));
				return receipts;
			case ANDROID:
				receipts.add(new AndroidReceipt(receipt));
				return receipts;
			case EDITOR:
				receipts.add(new EditorReceipt(receipt));
				return receipts;
		}

		throw InAppsErrors.invalidMarket();
	}
}
