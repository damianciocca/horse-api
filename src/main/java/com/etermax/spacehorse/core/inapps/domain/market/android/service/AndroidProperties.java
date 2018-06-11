package com.etermax.spacehorse.core.inapps.domain.market.android.service;

public class AndroidProperties {

	private String packageName;

	private String signature;

	public AndroidProperties(String packageName, String signature) {
		this.packageName = packageName;
		this.signature = signature;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSignature() {
		return signature;
	}

}
