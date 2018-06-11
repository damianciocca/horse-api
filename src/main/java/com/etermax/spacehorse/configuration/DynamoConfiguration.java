package com.etermax.spacehorse.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DynamoConfiguration {

	@JsonProperty("tablecreation")
	private Boolean tableCreation;

	@JsonProperty("endpoint")
	private String endpoint;

	@JsonProperty("accesskey")
	private String accessKey;

	@JsonProperty("secretkey")
	private String secretKey;

	public Boolean getTableCreation(){
		return this.tableCreation;
	}

	public AmazonDynamoDB buildClient() {
		return buildClient(this.endpoint);
	}

	public AmazonDynamoDB buildClient(String endpoint) {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient(getAwsCredentials(), getClientConfiguration());
		client.withEndpoint(endpoint);
		return client;
	}

	private ClientConfiguration getClientConfiguration() {
		return new ClientConfiguration(). //
				withThrottledRetries(true). //
				withMaxErrorRetry(5). //
				withMaxConnections(200);
	}

	private BasicAWSCredentials getAwsCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
}
