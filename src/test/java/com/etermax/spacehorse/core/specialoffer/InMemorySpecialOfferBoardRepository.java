package com.etermax.spacehorse.core.specialoffer;

import java.util.Map;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.exception.SpecialOfferBoardNotFoundExpception;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.google.common.collect.Maps;

public class InMemorySpecialOfferBoardRepository implements SpecialOfferBoardRepository {

	private final ServerTimeProvider timeProvider;
	private Map<String, SpecialOfferBoard> specialOffersByUserIds = Maps.newConcurrentMap();

	public InMemorySpecialOfferBoardRepository(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	@Override
	public SpecialOfferBoard findBy(Player player) {
		if (specialOffersByUserIds.containsKey(player.getUserId())) {
			return specialOffersByUserIds.get(player.getUserId());
		}
		throw new SpecialOfferBoardNotFoundExpception(player.getUserId());
	}

	@Override
	public SpecialOfferBoard findOrDefaultBy(Player player) {
		if (specialOffersByUserIds.containsKey(player.getUserId())) {
			return specialOffersByUserIds.get(player.getUserId());
		}
		return new SpecialOfferBoard(timeProvider);
	}

	@Override
	public void addOrUpdate(Player player, SpecialOfferBoard specialOfferBoard) {
		specialOffersByUserIds.put(player.getUserId(), specialOfferBoard);
	}

}
