package com.etermax.spacehorse.healthcheck;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.codahale.metrics.health.HealthCheck;

public class DynamoHealthCheck extends HealthCheck {

	private AmazonDynamoDB amazonDynamoDB;

	public DynamoHealthCheck(AmazonDynamoDB amazonDynamoDB) {
		this.amazonDynamoDB = amazonDynamoDB;
	}

	@Override
	protected Result check() throws Exception {
		try {
			amazonDynamoDB.listTables();
		} catch (Exception e) {
			return Result.unhealthy("Error to init AmazonDynamoDB: " + e.getMessage());
		}
		return Result.healthy();
	}

}