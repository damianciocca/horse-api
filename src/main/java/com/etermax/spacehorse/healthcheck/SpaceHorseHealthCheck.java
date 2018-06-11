package com.etermax.spacehorse.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class SpaceHorseHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}
}
