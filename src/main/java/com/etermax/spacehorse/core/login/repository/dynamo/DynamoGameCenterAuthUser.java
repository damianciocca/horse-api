package com.etermax.spacehorse.core.login.repository.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "auth_ios")
public class DynamoGameCenterAuthUser implements AbstractDynamoDAO {

    public static final String SECONDARY_INDEX_BY_GAME_CENTER_ID = "userIndexByGameCenterId";

    @DynamoDBHashKey(attributeName = "userId")
    private String userId;

    @DynamoDBIndexHashKey(attributeName = "gameCenterId", globalSecondaryIndexName = SECONDARY_INDEX_BY_GAME_CENTER_ID)
    private String gameCenterId;

    @DynamoDBAutoGeneratedTimestamp(strategy= DynamoDBAutoGenerateStrategy.CREATE)
    private Date createdDate;

    public DynamoGameCenterAuthUser() {
    }

    public DynamoGameCenterAuthUser(String userId) {
        this.userId = userId;
    }

    public void setId(String id) {
        this.userId = id;
    }

    @Override
    public String getId() {
        return getUserId();
    }

    public String getUserId(){
        return this.userId;
    }

    public String getGameCenterId() {
        return gameCenterId;
    }

    public void setGameCenterId(String gameCenterId) {
        this.gameCenterId = gameCenterId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}