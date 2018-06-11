package com.etermax.spacehorse.core.inapps.error;

public class InAppsErrors {

    public static final int INVALID_MARKET_CODE = 1900;

    public static final int INVALID_RECEIPT_CODE = 1902;

	public static final int DUPLICATE_RECEIPT_CODE = 1904;

	public static final int INVALID_CONFIGURATION_CODE = 1906;

	public static final int SERVER_TIMEOUT_CODE = 1908;

    private InAppsErrors() {
    }

    public static InAppsException invalidMarket() {
        return new InAppsException("Market not found", INVALID_MARKET_CODE);
    }

    public static InAppsException invalidReceipt() {
        return new InAppsException("Invalid receipt", INVALID_RECEIPT_CODE);
    }

	public static InAppsException duplicateReceipt() {
		return new InAppsException("Duplicate Receipt", DUPLICATE_RECEIPT_CODE);
	}

	public static InAppsException invalidConfiguration() {
		return new InAppsException("Invalid configuration", INVALID_CONFIGURATION_CODE);
	}

	public static InAppsException serverTimeout() {
		return new InAppsException("Server timeout while validating receipt", SERVER_TIMEOUT_CODE);
	}

}
