package com.etermax.spacehorse.core.user.repository.dynamo;

import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.mock.MockUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public final class UserDynamoRepositoryTest {

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();

	@Before
	public void setUp() { RULE.createSimpleTable(DynamoUser.class); }

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	private UserDynamoRepository repository;

	public UserDynamoRepositoryTest() {
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		this.repository = new UserDynamoRepository(dao);
	}

	@Test
	public void testFindByUserId() {
		User user = MockUtils.mockUser();
		this.repository.add(user);
		User found = this.repository.find(user.getUserId());

		assertNotNull(found);
		assertEquals(found, user);
	}

	@Test
	public void testNonExistentElementWhenFindBySecondaryIndex() {
		User found = this.repository.find("");
		assertNull(found);
	}

}
