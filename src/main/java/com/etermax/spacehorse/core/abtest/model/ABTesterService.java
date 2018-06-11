package com.etermax.spacehorse.core.abtest.model;

import com.etermax.abtester.domain.ABTestingTag;
import com.etermax.abtester.domain.ABTestingUser;
import com.etermax.abtester.domain.DefaultABTestingUser;
import com.etermax.abtester.domain.LifeState;
import com.etermax.abtester.factory.ABTesterServerConfig;
import com.etermax.abtester.factory.RestClientFactory;
import com.etermax.abtester.infrastructure.RestClient;
import com.etermax.spacehorse.configuration.ABTesterConfiguration;
import com.etermax.spacehorse.core.user.model.User;

import java.util.Optional;

public class ABTesterService {
    private String baseUrl;
    private String gameCode;
    private int readTimeoutInSeconds;
    private int connectTimeoutInSeconds;
    private int writeTimeoutInSeconds;
    private int requestTimeoutInSeconds;
    private String minAppVersion = "1.0";
    private RestClient restClient;

    public ABTesterService(ABTesterConfiguration abTesterConfiguration) {
        readTimeoutInSeconds = abTesterConfiguration.getReadTimeoutInSeconds();
        connectTimeoutInSeconds = abTesterConfiguration.getConnectTimeoutInSeconds();
        writeTimeoutInSeconds = abTesterConfiguration.getWriteTimeoutInSeconds();
        baseUrl = abTesterConfiguration.getBaseUrl();
        gameCode = abTesterConfiguration.getGameCode();
        requestTimeoutInSeconds = abTesterConfiguration.getRequestTimeoutInSeconds();
        ABTesterServerConfig config = new ABTesterServerConfig(baseUrl, gameCode,
                readTimeoutInSeconds, connectTimeoutInSeconds, writeTimeoutInSeconds, requestTimeoutInSeconds);
        restClient = new RestClientFactory().create(config);
    }

    public Optional<ABTestingTag> postulateNewUser(User user) {
        ABTestingUser abTestingUser = new DefaultABTestingUser.Builder()
                                                .withUserId(user.getUserId())
                                                .withLifeState(LifeState.New)
                                                .withAppVersion(minAppVersion)
                                                .build();
        return restClient.postulateUser(abTestingUser);
    }

    public Optional<String> findUserTag(String userId) {
        return restClient.findUserTag(userId).map(ABTestingTag::getValue);
    }
}
