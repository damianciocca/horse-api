package com.etermax.spacehorse.integration;

import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import com.etermax.spacehorse.configuration.SpaceHorseConfiguration;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.login.resource.request.LoginRequest;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.mock.MockUtils;
import com.jayway.jsonpath.JsonPath;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class BaseIntegrationTest {

	private static final String TEST_CONFIG_YML = "test-config.yml";
	@ClassRule
	public static final DropwizardAppRule<SpaceHorseConfiguration> RULE = new DropwizardAppRule<>(
			SpaceHorseApplicationTest.class, ResourceHelpers.resourceFilePath(TEST_CONFIG_YML));
	private static final String CATALOG_ID = "1507298083884";
	private static final String URL_SET_ACTIVE_CATALOG_ID = "/v1/catalog/setActiveCatalogId";
	private static final String SET_SERVER_AVAILABLE_ENDPOINT = "/v1/server/available";
	private static final String FORM_ADMIN_LOGIN_ID = "adminLoginId";
	private static final String FORM_ADMIN_PASSWORD = "adminPassword";
	private static final String FORM_CATALOG_ID = "catalogId";
	private static final String FORM_LAST_UPDATED = "lastUpdated";
	private static final String URL_LOGIN = "/v1/login";

	protected static final String LOGIN_ID_HEADER = "Login-Id";
	protected static final String SESSION_TOKEN_HEADER = "Session-Token";
	protected static int CLIENT_VERSION = 16;
	protected static Platform ANDROID_PLATFORM = Platform.ANDROID;
	protected static Platform IOS_PLATFORM = Platform.IOS;
	protected static Platform EDITOR_PLATFORM = Platform.EDITOR;
	protected static Client client = new JerseyClientBuilder().build();

	@BeforeClass
	public static void init() {
		persistCatalog();
		activateCatalogWith(CATALOG_ID);
		setServerStatusAvailable();
	}

	protected String aNewPlayerLogged() {
		return loginNewPlayer(ANDROID_PLATFORM);
	}

	protected String aNewEditorPlayerLogged() {
		return loginNewPlayer(EDITOR_PLATFORM);
	}

	protected String aNewIosPlayerLogged() {
		return loginNewPlayer(IOS_PLATFORM);
	}

	protected String mapToJsonStringFrom(Response response) {
		return response.readEntity(String.class);
	}

	protected String extractPlayerIdFrom(String responseAsJson) {
		return JsonPath.read(responseAsJson, "$.loginId");
	}

	protected String extractSessionTokenFrom(String responseAsJson) {
		return JsonPath.read(responseAsJson, "$.sessionToken");
	}

	protected Response loginPlayer(LoginRequest loginRequest) {
		return client.target(getLoginEndpoint()).request().post(entity(loginRequest, APPLICATION_JSON));
	}

	protected String getLoginEndpoint() {
		return getBaseUrl().concat(URL_LOGIN);
	}

	protected PlayerRepository getPlayerRepository() {
		return getApplication().getPlayerRepository();
	}

	protected CaptainCollectionRepository getCaptainCollectionRepository() {
		return getApplication().getCaptainsCollectionRepository();
	}

	protected static String getBaseUrl() {
		return format(("http://localhost:" + RULE.getLocalPort()));
	}

	private String loginNewPlayer(Platform platform) {
		LoginRequest loginRequest = new LoginRequest("", "", CLIENT_VERSION, platform);
		Response response = loginPlayer(loginRequest);
		return mapToJsonStringFrom(response);
	}

	private static void persistCatalog() {
		CatalogRepository catalogRepository = getApplication().getCatalogRepository();
		catalogRepository.add(MockUtils.mockCatalog());
	}

	private static void activateCatalogWith(String lastUpdatedId) {
		Form f = aActiveCatalogFormEntity(lastUpdatedId);
		Response response = client.target(getSetActiveCatalogEndpoint()).request()
				.post(entity(f, APPLICATION_FORM_URLENCODED));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	private static Form aActiveCatalogFormEntity(String lastUpdatedId) {
		Form form = new Form();
		form.param(FORM_ADMIN_LOGIN_ID, "admin");
		form.param(FORM_ADMIN_PASSWORD, "123456");
		form.param(FORM_CATALOG_ID, "1507298083884");
		form.param(FORM_LAST_UPDATED, lastUpdatedId);
		return form;
	}

	private static void setServerStatusAvailable() {
		Form f = aEmptyFormEntity();
		Response response = client.target(getSetServerAvailableEndpoint()).request()
				.post(entity(f, APPLICATION_FORM_URLENCODED));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	private static Form aEmptyFormEntity() {
		Form form = new Form();
		form.param(FORM_ADMIN_LOGIN_ID, "admin");
		form.param(FORM_ADMIN_PASSWORD, "123456");
		return form;
	}

	private static String getSetServerAvailableEndpoint() {
		return getBaseUrl().concat(SET_SERVER_AVAILABLE_ENDPOINT);
	}

	private static String getSetActiveCatalogEndpoint() {
		return getBaseUrl().concat(URL_SET_ACTIVE_CATALOG_ID);
	}

	private static SpaceHorseApplicationTest getApplication() {
		return RULE.getApplication();
	}

	protected Catalog getActiveCatalog() {
		return getApplication().getCatalogRepository().find(CATALOG_ID);
	}

	protected FixedServerTimeProvider getServerTimeProvider() {
		return (FixedServerTimeProvider) getApplication().getServerTimeProvider();
	}

	protected QuestBoardRepository getQuestBoardRepository() {
		return getApplication().getQuestBoardRepository();
	}

	protected AchievementCollectionRepository getAchievementCollectionRepository() {
		return getApplication().getAchievementCollectionRepository();
	}

}
