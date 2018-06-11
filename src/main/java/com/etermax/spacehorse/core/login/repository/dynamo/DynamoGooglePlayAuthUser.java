package com.etermax.spacehorse.core.login.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

import java.util.Date;

@DynamoDBTable(tableName = "auth")
public class DynamoGooglePlayAuthUser implements AbstractDynamoDAO {

    public static final String SECONDARY_INDEX_BY_GOOGLE_PLAY_ID = "userIndexByGooglePlayId";

    @DynamoDBHashKey(attributeName = "userId")
    private String userId;

    @DynamoDBIndexHashKey(attributeName = "googlePlayId", globalSecondaryIndexName = SECONDARY_INDEX_BY_GOOGLE_PLAY_ID)
    private String googlePlayId;

    @DynamoDBAutoGeneratedTimestamp(strategy= DynamoDBAutoGenerateStrategy.CREATE)
    private Date createdDate;

    public DynamoGooglePlayAuthUser() {
    }

    public DynamoGooglePlayAuthUser(String userId) {
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

    public String getGooglePlayId() {
        return googlePlayId;
    }

    public void setGooglePlayId(String googlePlayId) {
        this.googlePlayId = googlePlayId;
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