package com.etermax.spacehorse.core.login.resource.response.eventsnotification;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.specialoffer.model.NextSpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;

public class ScheduledNotificationEventsFactory {

	public ScheduledNotificationEventsResponse create(Player player, List<SpecialOfferDefinition> specialOfferDefinitions,
			SpecialOfferBoard specialOfferBoard) {
		List<NextSpecialOffer> nextRefreshTimeOfSpecialOffers = createNextScheduledSpecialOffers(player, specialOfferBoard, specialOfferDefinitions);
		return new ScheduledNotificationEventsResponse(nextRefreshTimeOfSpecialOffers);
	}

	private List<NextSpecialOffer> createNextScheduledSpecialOffers(Player player, SpecialOfferBoard specialOfferBoard,
			List<SpecialOfferDefinition> definitions) {
		return specialOfferBoard.getNextSpecialOffers(player, definitions);
	}

}
