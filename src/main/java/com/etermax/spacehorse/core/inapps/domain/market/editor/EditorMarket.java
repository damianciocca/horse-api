package com.etermax.spacehorse.core.inapps.domain.market.editor;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.inapps.domain.Receipt;

public class EditorMarket implements Market {

	@Override
	public boolean canBeUsedIn(EnviromentType enviromentType) {
		return enviromentType.equals(EnviromentType.INTEGRATION_TEST) //
				|| enviromentType.equals(EnviromentType.DEVELOPMENT) //
				|| enviromentType.equals(EnviromentType.STAGING);
	}

	public boolean validate(Receipt receipt) {
		receipt.setValid(true);
		return true;
	}

}
