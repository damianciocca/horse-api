package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.user.model.Platform;

public enum MarketType {
	EDITOR,
	ANDROID,
	IOS,
	UNKNOWN;

	public static MarketType getMarketType(Platform platform) {
		switch (platform) {
			case ANDROID:
				return MarketType.ANDROID;
			case IOS:
				return MarketType.IOS;
			case EDITOR:
				return MarketType.EDITOR;
			default:
				return MarketType.UNKNOWN;
		}
	}
}
