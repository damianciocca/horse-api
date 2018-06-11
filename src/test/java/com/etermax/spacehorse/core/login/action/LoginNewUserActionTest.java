package com.etermax.spacehorse.core.login.action;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.etermax.abtester.domain.ABTestingTag;
import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.ABTesterService;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.model.PlayerCreationDomainService;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class LoginNewUserActionTest {

	private static final String LOGIN_ID = "10";

	private LoginNewUserAction action;
	private String catalogId;
	private int MMR = 160;
	private Player player;

	@Before
	public void setUp() throws Exception {
		Catalog catalog = MockUtils.mockCatalog();
		catalogId = catalog.getCatalogId();
		player = new PlayerScenarioBuilder(LOGIN_ID).build();

		PlayerWinRateRepository playerWinRateRepository = aPlayerWinRateRepository();
		PlayerRepository playerRepository = aPlayerRepository();
		FixedServerTimeProvider timeProvider = new FixedServerTimeProvider();
		UserRepository userRepository = aUserRepository();
		CatalogAction catalogAction = aCatalogAction(catalog);
		ABTesterService abTesterService = anAbTesterService();
		PlayerCreationDomainService playerCreationDomainService = aPlayerCreationDomainService();

		action = new LoginNewUserAction(userRepository, playerRepository, Role.PLAYER, catalogAction, playerWinRateRepository, timeProvider, abTesterService,
				playerCreationDomainService, mock(PlayerLeagueService.class));
	}

	@Test
	public void whenLoginANewUserThenANewPlayerIsLoggedSuccessfully() throws Exception {
		// when
		LoginInfo loginInfo = whenLoginNewUser();
		// then
		thenAssertThatNewUserWasLogged(loginInfo);
	}

	private LoginInfo whenLoginNewUser() {
		return action.login(Platform.ANDROID);
	}

	private void thenAssertThatNewUserWasLogged(LoginInfo loginInfo) {
		assertThat(loginInfo.getUser().getUserId()).isNotNull();
		assertThat(loginInfo.getLatestCatalogId()).isEqualTo(catalogId.concat("-"));
		assertThat(loginInfo.getMmr()).isEqualTo(MMR);
		assertThat(loginInfo.getPassword()).isNotNull();
		assertThat(loginInfo.getPlayer().getUserId()).isNotNull();
		assertThat(loginInfo.getPlayer().getCatalogId()).isEqualTo(catalogId);
	}

	private PlayerCreationDomainService aPlayerCreationDomainService() {
		PlayerCreationDomainService playerCreationDomainService = mock(PlayerCreationDomainService.class);
		when(playerCreationDomainService.createPlayer(anyString(), any(ABTag.class), any(Catalog.class), anyLong())).thenReturn(player);
		return playerCreationDomainService;
	}

	private ABTesterService anAbTesterService() {
		ABTesterService abTesterService = mock(ABTesterService.class);
		ABTestingTag abTestingTag = mock(ABTestingTag.class);
		when(abTesterService.postulateNewUser(any(User.class))).thenReturn(of(abTestingTag));
		return abTesterService;
	}

	private PlayerWinRateRepository aPlayerWinRateRepository() {
		PlayerWinRateRepository repository = mock(PlayerWinRateRepository.class);
		when(repository.findOrCrateDefault(anyString())).thenReturn(new PlayerWinRate(LOGIN_ID, 2, 4, 1, MMR));
		return repository;
	}

	private PlayerRepository aPlayerRepository() {
		PlayerRepository repository = mock(PlayerRepository.class);
		return repository;
	}

	private CatalogAction aCatalogAction(Catalog catalog) {
		CatalogAction catalogAction = Mockito.mock(CatalogAction.class);
		when(catalogAction.getCatalogForUser(any(ABTag.class))).thenReturn(catalog);
		return catalogAction;
	}

	private UserRepository aUserRepository() {
		return Mockito.mock(UserRepository.class);
	}
}
