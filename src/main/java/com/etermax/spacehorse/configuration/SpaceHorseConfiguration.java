package com.etermax.spacehorse.configuration;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.club.ClubReporterConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SpaceHorseConfiguration extends Configuration implements AssetsBundleConfiguration {

	@JsonProperty("authentication")
	@NotNull
	private AuthenticationConfiguration authenticationConfiguration;

	@JsonProperty("dynamoConfiguration")
	@NotNull
	private DynamoConfiguration dynamoConfiguration;

	@JsonProperty("environment")
	@NotNull
	private EnviromentType environment;

	@JsonProperty("realtimeServer")
	@NotNull
	private RealtimeServerConfiguration realtimeServerConfiguration;

	@JsonProperty("inAppsAndroid")
	@NotNull
	private InAppsAndroidConfiguration inAppsAndroidConfiguration;

	@JsonProperty("inAppsIos")
	@NotNull
	private InAppsIosConfiguration inAppsIosConfiguration;

	@JsonProperty("expectedClientVersion")
	@NotNull
	private Integer expectedClientVersion;

	@JsonProperty("repositoryType")
	@NotNull
	private String repositoryType;

    @Valid
    @NotNull
    @JsonProperty("assets")
    private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

	@JsonProperty("matchmakingServerUrl")
	@NotNull
	private String matchmakingServerUrl;

	@JsonProperty("abTesterConfiguration")
	@NotNull
	private ABTesterConfiguration abTesterConfiguration;

	@JsonProperty("clubReporterConfiguration")
	@NotNull
	private ClubReporterConfiguration clubReporterConfiguration;

	@Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }

	public AuthenticationConfiguration getAuthenticationConfiguration() {
		return authenticationConfiguration;
	}

	public EnviromentType getEnvironment() {
		return environment;
	}

	public Role getEnvironmentRole() {
		return (getEnvironment().equals(EnviromentType.DEVELOPMENT) || getEnvironment()
				.equals(EnviromentType.STAGING)) ? Role.TESTER : Role.PLAYER;
	}

	public RealtimeServerConfiguration getRealtimeServerConfiguration() {
		return realtimeServerConfiguration;
	}

	public InAppsAndroidConfiguration getInAppsAndroidConfiguration() {
		return inAppsAndroidConfiguration;
	}

	public InAppsIosConfiguration getInAppsIosConfiguration() {
		return inAppsIosConfiguration;
	}

	public Integer getExpectedClientVersion() {
		return this.expectedClientVersion;
	}

	public String getRepositoryType() {
		return this.repositoryType;
	}

	public DynamoConfiguration getDynamoConfiguration() {
		return this.dynamoConfiguration;
	}

	public String getMatchmakingServerUrl() {
		return this.matchmakingServerUrl;
	}

	public ABTesterConfiguration getAbTesterConfiguration() {
		return this.abTesterConfiguration;
	}

	public ClubReporterConfiguration getClubReporterConfiguration() {
		return this.clubReporterConfiguration;
	}
}
