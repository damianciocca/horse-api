package com.etermax.spacehorse.core.login.repository.dynamo;

import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DynamoAuthRepositoryTest {

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();

	@Before
	public void setUp() {
		RULE.createTableWithGlobalSecondaryIndexes(DynamoGooglePlayAuthUser.class);
		RULE.createTableWithGlobalSecondaryIndexes(DynamoGameCenterAuthUser.class);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	private DynamoAuthRepository repository;

	public DynamoAuthRepositoryTest() {
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		this.repository = new DynamoAuthRepository(dao);
	}

	@Test
	public void testUnableToFindAndroid() {
		assertNull(this.repository.findById(Platform.ANDROID,"lalala"));
	}

	@Test
	public void testUnableToFindIos() {
		assertNull(this.repository.findById(Platform.IOS,"lalala"));
	}

	@Test
	public void testSucessfulLinkAndroid() {
		String loginId = "loginId";
		String googlePlayUserId = "googlePlayUserId";
		this.repository.link(Platform.ANDROID, loginId, googlePlayUserId);

		assertEquals(loginId, this.repository.findById(Platform.ANDROID, googlePlayUserId));
	}

	@Test
	public void testSucessfulLinkIos() {
		String loginId = "loginId";
		String gameCenterUserId = "gameCenterUserId";
		this.repository.link(Platform.IOS, loginId, gameCenterUserId);

		assertEquals(loginId, this.repository.findById(Platform.IOS, gameCenterUserId));
	}
}
