package com.etermax.spacehorse.core.inapps.domain;

import com.etermax.spacehorse.configuration.EnviromentType;

public interface Market {
	boolean canBeUsedIn(EnviromentType enviromentType);
	boolean validate(Receipt receipt);
}
