package com.etermax.spacehorse.core.user.repository.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.login.repository.dynamo.DynamoGameCenterAuthUser;
import com.etermax.spacehorse.core.login.repository.dynamo.DynamoGooglePlayAuthUser;
import com.etermax.spacehorse.core.player.repository.dynamo.DynamoPlayer;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DynamoDao<T> {

	private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;
    private final DynamoDB dynamoDB;

    public DynamoDao(AmazonDynamoDB client) {
        Validate.notNull(client);
        this.client = client;
        this.mapper = new DynamoDBMapper(client);
        this.dynamoDB = new DynamoDB(client);
    }

	public DynamoUser findByUserId(String userId) {
        DynamoUser searchDynamoUser = new DynamoUser();
        searchDynamoUser.setUserId(userId);
        return (DynamoUser) this.find(searchDynamoUser);
    }

    public Optional<DynamoGooglePlayAuthUser> findByGooglePlayUserId(String googlePlayUserId) {
        DynamoGooglePlayAuthUser searchDynamoAuthUser = new DynamoGooglePlayAuthUser();
        searchDynamoAuthUser.setGooglePlayId(googlePlayUserId);
        List<AbstractDynamoDAO> result = findBySecondaryIndex(DynamoGooglePlayAuthUser.class, DynamoGooglePlayAuthUser.SECONDARY_INDEX_BY_GOOGLE_PLAY_ID, searchDynamoAuthUser);
        return result.stream().map(abstractDAO -> (DynamoGooglePlayAuthUser) abstractDAO).findFirst();
    }

    public Optional<DynamoGameCenterAuthUser> findByGameCenterUserId(String gameCenterUserId) {
        DynamoGameCenterAuthUser searchDynamoAuthUser = new DynamoGameCenterAuthUser();
        searchDynamoAuthUser.setGameCenterId(gameCenterUserId);
        List<AbstractDynamoDAO> result = findBySecondaryIndex(DynamoGameCenterAuthUser.class, DynamoGameCenterAuthUser.SECONDARY_INDEX_BY_GAME_CENTER_ID, searchDynamoAuthUser);
        return result.stream().map(abstractDAO -> (DynamoGameCenterAuthUser) abstractDAO).findFirst();
    }

    public void update(AbstractDynamoDAO entity) {
        this.mapper.save(entity);
    }

    public void add(AbstractDynamoDAO entity) {
        this.mapper.save(entity);
    }

    public AbstractDynamoDAO find(AbstractDynamoDAO entity) {
        return this.mapper.load(entity);
    }

    public T find(Class<T> clazz, String hashKey) {
        return this.mapper.load(clazz, hashKey);
    }

    public Boolean isDuplicated(AbstractDynamoDAO entity) {
        AbstractDynamoDAO load = this.mapper.load(entity);
        return (load != null);
    }

    public void remove(AbstractDynamoDAO entity) {
        this.mapper.delete(entity);
    }

    public void remove(List<AbstractDynamoDAO> abstractDynamoDAOs) { this.mapper.batchDelete(abstractDynamoDAOs); }

    public List<AbstractDynamoDAO> findBySecondaryIndex(Class clazz, String secondaryIndexName, AbstractDynamoDAO secondaryIndexHashKeyValue) {
        DynamoDBQueryExpression<AbstractDynamoDAO> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withIndexName(secondaryIndexName).withConsistentRead(false).withHashKeyValues(secondaryIndexHashKeyValue);
        PaginatedQueryList<AbstractDynamoDAO> query = this.mapper.query(clazz, queryExpression);
        if (query.stream().findFirst().isPresent()) {
            return query.stream().collect(Collectors.toList());
        }
        return new ArrayList();
    }

    public void update(UpdateItemRequest updateItemRequest) {
    	this.client.updateItem(updateItemRequest);
	}

    public Table getTable(String tableName) {
        return dynamoDB.getTable(tableName);
    }

    public List<T> findInBatch(List<String> ids, String tableName, Class<T> dynamoDocumentClass) {

        List<KeyPair> keyPairList = new ArrayList<>();
        ids.forEach(id -> keyPairList.add(new KeyPair().withHashKey(id)));

        Map<Class<?>, List<KeyPair>> keyPairForTable = new HashMap<>();
        keyPairForTable.put(dynamoDocumentClass, keyPairList);

        Map<String, List<Object>> batchResults = mapper.batchLoad(keyPairForTable);

       return batchResults.get(tableName).stream().map(dynamoDocumentClass::cast).collect(Collectors.toList());
    }
}
