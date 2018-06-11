package com.etermax.spacehorse.core.login.resource.response;

import com.etermax.spacehorse.core.login.action.LoginInfo;
import com.etermax.spacehorse.core.login.resource.response.eventsnotification.ScheduledNotificationEventsFactory;
import com.etermax.spacehorse.core.login.resource.response.eventsnotification.ScheduledNotificationEventsResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.PlayerResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.model.User;

public class LoginResponseFactory {

	private final String matchmakingServerURL;
	private final ServerTimeProvider timeProvider;
	private final PlayerResponseFactory playerResponseFactory;
	private final ScheduledNotificationEventsFactory scheduledNotificationEventsFactory;

	public LoginResponseFactory(String matchmakingServerURL, ServerTimeProvider timeProvider, PlayerResponseFactory playerResponseFactory) {
		this.matchmakingServerURL = matchmakingServerURL;
		this.timeProvider = timeProvider;
		this.playerResponseFactory = playerResponseFactory;
		this.scheduledNotificationEventsFactory = new ScheduledNotificationEventsFactory();
	}

	public LoginResponse createFrom(LoginInfo loginInfo) {
		Player player = loginInfo.getPlayer();
		User user = loginInfo.getUser();
		String latestCatalogId = loginInfo.getLatestCatalogId();
		int mmr = loginInfo.getMmr();
		PlayerResponse playerResponse = playerResponseFactory.createFrom(player, mmr, loginInfo.getPlayerSeasons());

		ScheduledNotificationEventsResponse scheduledNotificationEvents = scheduledNotificationEventsFactory
				.create(player, loginInfo.getSpecialOfferDefinitions(), playerResponse.getSpecialOfferBoard());

		return new LoginResponse(user.getUserId(), loginInfo.getPassword(), user.getSessionToken(), user.getRole(), latestCatalogId, playerResponse,
				mmr, matchmakingServerURL, timeProvider.getTimeNowAsSeconds(), loginInfo.getLinkedWithSocialAccountId(), scheduledNotificationEvents);
	}
}
