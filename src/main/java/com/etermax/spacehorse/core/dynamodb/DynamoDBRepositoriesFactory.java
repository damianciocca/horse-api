package com.etermax.spacehorse.core.dynamodb;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.logging.log4j.util.Strings.EMPTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.etermax.spacehorse.app.HorseRaceRepositories;
import com.etermax.spacehorse.configuration.DynamoConfiguration;
import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.configuration.SpaceHorseConfiguration;
import com.etermax.spacehorse.core.achievements.repository.DynamoAchievementCollectionRepository;
import com.etermax.spacehorse.core.ads.videorewards.repository.quota.DynamoQuotaVideoRewardRepository;
import com.etermax.spacehorse.core.battle.model.DefaultBattleFactory;
import com.etermax.spacehorse.core.battle.repository.dynamo.BattleDynamoRepository;
import com.etermax.spacehorse.core.battle.repository.dynamo.PlayerWinRateDynamoRepository;
import com.etermax.spacehorse.core.capitain.infrastructure.DynamoCaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.catalog.repository.dynamo.CatalogDynamoRepository;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.liga.repository.DynamoPlayerLeagueRepository;
import com.etermax.spacehorse.core.login.repository.dynamo.DynamoAuthRepository;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.quest.repository.DynamoQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.repository.DynamoSpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.core.user.repository.dynamo.UserDynamoRepository;
import com.google.common.collect.Lists;

public class DynamoDBRepositoriesFactory {

	private static final Logger logger = LoggerFactory.getLogger(DynamoDBRepositoriesFactory.class);
	private static final String DYNAMODB_URL = "DYNAMODB_URL";

	public static HorseRaceRepositories createRepositories(SpaceHorseConfiguration configuration, ServerTimeProvider serverTimeProvider) {
		HorseRaceRepositories horseRaceRepositories = new HorseRaceRepositories();
		DynamoConfiguration dynamoConfiguration = configuration.getDynamoConfiguration();
		AmazonDynamoDB dynamoClient = buildClient(dynamoConfiguration, configuration);

		if (dynamoConfiguration.getTableCreation()) {
			createTablesIfNonExist(dynamoClient);
		}

		DynamoDao dao = new DynamoDao(dynamoClient);

		horseRaceRepositories.setUserRepository(new UserDynamoRepository(dao));
		horseRaceRepositories.setBattleRepository(new BattleDynamoRepository(dao, new DefaultBattleFactory()));
		horseRaceRepositories.setPlayerWinRateRepository(new PlayerWinRateDynamoRepository(dao));
		horseRaceRepositories.setAuthRepository(new DynamoAuthRepository(dao));
		ServerSettingsRepository serverSettingsRepository = new ServerSettingsRepository(dao);
		horseRaceRepositories.setServerSettings(serverSettingsRepository);
        horseRaceRepositories.setCatalogRepository(new CatalogDynamoRepository(dao, serverSettingsRepository, serverTimeProvider));
		horseRaceRepositories.setPlayerRepository(new PlayerDynamoRepository(dao, horseRaceRepositories.getCatalogRepository(), serverTimeProvider));
		horseRaceRepositories.setMarketRepository(new MarketRepository(dao));
		horseRaceRepositories
				.setQuestBoardRepository(new DynamoQuestBoardRepository(dao, serverTimeProvider, horseRaceRepositories.getCatalogRepository()));
		horseRaceRepositories.setSpecialOfferBoardRepository(
				new DynamoSpecialOfferBoardRepository(dao, serverTimeProvider, horseRaceRepositories.getCatalogRepository()));
		horseRaceRepositories
				.setCaptainCollectionRepository(new DynamoCaptainCollectionRepository(dao, horseRaceRepositories.getCatalogRepository()));
		horseRaceRepositories.setQuotaVideoRewardRepository(new DynamoQuotaVideoRewardRepository(dao, serverTimeProvider));
		horseRaceRepositories
				.setAchievementCollectionRepository(new DynamoAchievementCollectionRepository(dao, horseRaceRepositories.getCatalogRepository()));
		horseRaceRepositories.setPlayerLeagueRepository(new DynamoPlayerLeagueRepository(dao));

		return horseRaceRepositories;
	}

	private static AmazonDynamoDB buildClient(DynamoConfiguration dynamoConfiguration, SpaceHorseConfiguration configuration) {
		EnviromentType environment = configuration.getEnvironment();
		logger.info("environment = [ {} ]", environment);
		if (EnviromentType.INTEGRATION_TEST.equals(environment)) {
			return buildClientForIntegrationTestEnvironment(dynamoConfiguration);
		}
		return dynamoConfiguration.buildClient();
	}

	private static AmazonDynamoDB buildClientForIntegrationTestEnvironment(DynamoConfiguration dynamoConfiguration) {
		String dynamoDbUrl = ofNullable(System.getenv(DYNAMODB_URL)).orElse(EMPTY);
		logger.info("dynamoDbUrl = [ {} ]", dynamoDbUrl);
		if (isBlank(dynamoDbUrl)) {
			return dynamoConfiguration.buildClient();
		}
		return dynamoConfiguration.buildClient(dynamoDbUrl);
	}

	private static List<AttributeDefinition> setAttributesDefinitions(HashMap<String, String> attributesDefinitionsMap) {
		List<AttributeDefinition> attributesDefinitions = new ArrayList<>();
		attributesDefinitionsMap.entrySet().forEach(attrDef -> {
			String attrKey = attrDef.getKey();
			String attrValue = attrDef.getValue();
			AttributeDefinition attributeDefinition = new AttributeDefinition(attrKey, attrValue);
			attributesDefinitions.add(attributeDefinition);
		});
		return attributesDefinitions;
	}

	private static void createTablesIfNonExist(AmazonDynamoDB dynamoClient) {
		Long readCapacity = 10l;
		Long writeCapacity = 10l;
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(readCapacity, writeCapacity);

		createUserTable(dynamoClient, provisionedThroughput);
		createPlayerTableRequest(dynamoClient, provisionedThroughput);
		createBattleTableRequest(dynamoClient, provisionedThroughput);
		createWinLoseRateTable(dynamoClient, provisionedThroughput);
		createCatalogTableRequest(dynamoClient, provisionedThroughput);
		createAuthTables(dynamoClient, provisionedThroughput);
		createPurchaseTables(dynamoClient, provisionedThroughput);
		createServerSettingsTableRequest(dynamoClient, provisionedThroughput);
		createQuestBoardTableRequest(dynamoClient, provisionedThroughput);
		createSpecialOfferBoardTableRequest(dynamoClient, provisionedThroughput);
		createCaptainsCollectionTableRequest(dynamoClient, provisionedThroughput);
		createQuotaVideoRewardTableRequest(dynamoClient, provisionedThroughput);
		createAchievementCollectionTableRequest(dynamoClient, provisionedThroughput);
		createAPlayerSeasonsTableRequest(dynamoClient, provisionedThroughput);
	}

	private static void createPurchaseTables(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		createPurchaseTable(dynamoClient, provisionedThroughput, "android_purchase", "androidPurchaseIndexbyUserId");
		createPurchaseTable(dynamoClient, provisionedThroughput, "ios_purchase", "iosPurchaseIndexbyUserId");
	}

	private static void createCatalogTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("catalogId", "S");
		attrsDefs.put("lastUpdated", "N");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement1 = new KeySchemaElement("catalogId", "HASH");
		KeySchemaElement keySchemaElement2 = new KeySchemaElement("lastUpdated", "RANGE");
		keySchema.add(keySchemaElement1);
		keySchema.add(keySchemaElement2);

		CreateTableRequest createCatalogTableRequest = new CreateTableRequest(attributeDefinitions, "catalog", keySchema, provisionedThroughput);

		TableUtils.createTableIfNotExists(dynamoClient, createCatalogTableRequest);
	}

	private static void createServerSettingsTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("id", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("id", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createServerSettingsTableRequest = new CreateTableRequest(attributeDefinitions, "serverSettings", keySchema,
				provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createServerSettingsTableRequest);
	}

	private static void createBattleTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("battleId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("battleId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createBattleTableRequest = new CreateTableRequest(attributeDefinitions, "battle", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createBattleTableRequest);
	}

	private static void createPlayerTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createPlayerTableRequest = new CreateTableRequest(attributeDefinitions, "player", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createPlayerTableRequest);
	}

	private static void createUserTable(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createUserTableRequest = new CreateTableRequest(attributeDefinitions, "user", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createUserTableRequest);
	}

	private static void createAuthTables(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		createAuthTableRequest(dynamoClient, provisionedThroughput, "auth", "googlePlayId", "userIndexByGooglePlayId");
		createAuthTableRequest(dynamoClient, provisionedThroughput, "auth_ios", "gameCenterId", "userIndexByGameCenterId");
	}

	private static void createAuthTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput, String tableName,
			String keyName, String userIndexName) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put(keyName, "S");
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createUserTableRequest = new CreateTableRequest(attributeDefinitions, tableName, keySchema, provisionedThroughput);

		List<KeySchemaElement> keySchemaIndex = new ArrayList<>();
		keySchemaIndex.add(new KeySchemaElement(keyName, "HASH"));
		Projection projection = new Projection().withProjectionType("ALL");
		createUserTableRequest.withGlobalSecondaryIndexes(
				new GlobalSecondaryIndex().withIndexName(userIndexName).withKeySchema(keySchemaIndex).withProjection(projection)
						.withProvisionedThroughput(provisionedThroughput));

		TableUtils.createTableIfNotExists(dynamoClient, createUserTableRequest);
	}

	private static void createWinLoseRateTable(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createWLRateTableRequest = new CreateTableRequest(attributeDefinitions, "playerWinRate", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createWLRateTableRequest);
	}

	private static void createPurchaseTable(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput, String tableName,
			String userIndexName) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("transactionId", "S");
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = new ArrayList<>();
		KeySchemaElement keySchemaElement = new KeySchemaElement("transactionId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, tableName, keySchema, provisionedThroughput);

		Projection projection = new Projection().withProjectionType("ALL");
		List<KeySchemaElement> userKeySchemaIndex = new ArrayList<>();
		userKeySchemaIndex.add(new KeySchemaElement("userId", "HASH"));

		GlobalSecondaryIndex userIdGlobalSecondaryIndex = new GlobalSecondaryIndex().withIndexName(userIndexName).withKeySchema(userKeySchemaIndex)
				.withProjection(projection).withProvisionedThroughput(provisionedThroughput);
		createTableRequest.withGlobalSecondaryIndexes(userIdGlobalSecondaryIndex);

		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createQuestBoardTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "questBoard", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createSpecialOfferBoardTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "specialOfferBoard", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createCaptainsCollectionTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "captainsCollection", keySchema, provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createQuotaVideoRewardTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		attrsDefs.put("placeName", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement1 = new KeySchemaElement("userId", "HASH");
		KeySchemaElement keySchemaElement2 = new KeySchemaElement("placeName", "RANGE");
		keySchema.add(keySchemaElement1);
		keySchema.add(keySchemaElement2);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "quotaVideoRewards", keySchema,
				provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createAchievementCollectionTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "achievementsCollection", keySchema,
				provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

	private static void createAPlayerSeasonsTableRequest(AmazonDynamoDB dynamoClient, ProvisionedThroughput provisionedThroughput) {
		HashMap<String, String> attrsDefs = new HashMap<>();
		attrsDefs.put("userId", "S");
		List<AttributeDefinition> attributeDefinitions = setAttributesDefinitions(attrsDefs);

		List<KeySchemaElement> keySchema = Lists.newArrayList();
		KeySchemaElement keySchemaElement = new KeySchemaElement("userId", "HASH");
		keySchema.add(keySchemaElement);

		CreateTableRequest createTableRequest = new CreateTableRequest(attributeDefinitions, "playerLeague", keySchema,
				provisionedThroughput);
		TableUtils.createTableIfNotExists(dynamoClient, createTableRequest);
	}

}