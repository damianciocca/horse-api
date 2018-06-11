package com.etermax.spacehorse.core.common.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.junit.rules.ExternalResource;

public class LocalDynamoDBCreationRule extends ExternalResource {

	private DynamoDBMapper dynamoDBMapper;

	private AmazonDynamoDB amazonDynamoDB;

	public LocalDynamoDBCreationRule() {
		System.setProperty("sqlite4java.library.path", "native-libs");
		amazonDynamoDB = DynamoDBEmbedded.create();
		dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
	}

	public DynamoDBMapper getDynamoDBMapper() {
		return dynamoDBMapper;
	}

	public AmazonDynamoDB getAmazonDynamoDB() {
		return amazonDynamoDB;
	}

	public void deleteAllTables() {
		amazonDynamoDB.listTables().getTableNames().forEach(amazonDynamoDB::deleteTable);
	}

	public void deleteTable(String tableName) { amazonDynamoDB.deleteTable(tableName); }

	@Override
	protected void before() throws Throwable {
		try {
			System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Slf4jLog");
			amazonDynamoDB = DynamoDBEmbedded.create();
			dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void after() {
		if (amazonDynamoDB == null) {
			return;
		}
		try {
			amazonDynamoDB.shutdown();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void createSimpleTable(Class aClass) {
		CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(aClass, DynamoDBMapperConfig.DEFAULT);
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(10L, 10L);
		createTableRequest.withProvisionedThroughput(provisionedThroughput);
		TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
	}

	public void createTableWithGlobalSecondaryIndexes(Class aClass) {
		CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(aClass, DynamoDBMapperConfig.DEFAULT);
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(10L, 10L);
		Projection projection = new Projection().withProjectionType(ProjectionType.ALL);

		if (createTableRequest.getGlobalSecondaryIndexes() != null) {
			int globalSecondaryIndexesSize = createTableRequest.getGlobalSecondaryIndexes().size();
			for (int i = 0; i < globalSecondaryIndexesSize; i++) {
				createTableRequest.withProvisionedThroughput(provisionedThroughput).getGlobalSecondaryIndexes().get(i)
						.withProvisionedThroughput(provisionedThroughput).setProjection(projection);
			}
		} else {
			createTableRequest.withProvisionedThroughput(provisionedThroughput);
		}

		TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
	}

}
