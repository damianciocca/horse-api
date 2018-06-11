package com.etermax.spacehorse.core.inapps.domain;

public abstract class Receipt {

	private boolean valid;

	public boolean isValid() {
    	return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public abstract String getProductId();

}
