package com.etermax.spacehorse.core.matchmaking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.matchmaking.model.match.Match;

public abstract class MatchmakingService<T extends MatchmakingQueueEntry> {

	private static final Logger logger = LoggerFactory.getLogger(MatchmakingService.class);

	final protected CatalogRepository catalogRepository;

	final private Thread thread;
	final private BlockingDeque<T> queue = new LinkedBlockingDeque<>();
	final private List<T> pendingMatchmakings = new ArrayList<>();

	public MatchmakingService(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
		this.thread = new Thread(() -> runMatchmakingLoop());
		this.thread.start();
	}

	public void enqueueRequest(T request) {
		queue.add(request);
	}

	private void runMatchmakingLoop() {
		while (true) {
			try {
				if (matchPlayersAndStartBattles() == 0) {
					//No match started, sleep the thread some time so it doesn't consumes 100% CPU.. I'm sure
					//that we can convert this algorithm to a blocking one that doesn't need this ugly loop
					//and this sleep() call.. maybe when we have more time..
					sleep();
				}
			} catch (Exception ex) {
				logger.error("while match making", ex);
			}
		}
	}

	private int matchPlayersAndStartBattles() {

		getNewPendingMatchesAndDisconnectDuplicatedPlayers();

		//This is really ugly.. but I didn't found a more performant way of doing it.
		//What we are trying to do is match the players in the in pendingMatchmakings with any of the other players
		//in the list, giving priority to the players that have been waiting the most.
		//The algorithm is as follow:
		// -- For each entry in the list
		//   -- try to match it against any of the other items in the list
		//   -- if a match if found, remove both entries from the list and call startMatch()
		//   -- if no match is found, and the entry has been waiting too long, remove it from the list and call
		//      onHasBeenWaitingTooLong().

		int numberOfMatchesStarted = 0;
		int index = 0;

		while (index < pendingMatchmakings.size()) {
			T e1 = pendingMatchmakings.get(index);
			Match match = tryToMatch(e1, index + 1);

			if (match != null) {
				pendingMatchmakings.remove(match.getEntry1());
				match.getEntry2().ifPresent(pendingMatchmakings::remove);
				numberOfMatchesStarted++;
			} else if (e1.isResponseSent()) {
				pendingMatchmakings.remove(e1);
			} else {
				index++;
			}
		}

		return numberOfMatchesStarted;
	}

	private Match tryToMatch(T queueEntry, int fromIndex) {
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(ABTag.emptyABTag());
		Optional<T> opponent = findOpponent(catalog, queueEntry, fromIndex);
		if (opponent.isPresent()) {
			return startMatch(catalog, queueEntry, opponent.get());
		}
		if (hasBeenWaitingTooLong(catalog, queueEntry)) {
			return onHasBeenWaitingTooLong(catalog, queueEntry);
		}
		return null;
	}

	protected abstract Match startMatch(Catalog catalog, T e1, MatchmakingQueueEntry e2);

	protected abstract boolean doPlayersMatch(Catalog catalog, T e1, T e2);

	protected abstract boolean hasBeenWaitingTooLong(Catalog catalog, MatchmakingQueueEntry e1);

	protected abstract Match onHasBeenWaitingTooLong(Catalog catalog, T queueEntry);

	private Optional<T> findOpponent(Catalog catalog, T e1, int fromIndex) {
		for (int j = fromIndex; j < pendingMatchmakings.size(); j++) {
			T e2 = pendingMatchmakings.get(j);
			if (doPlayersMatch(catalog, e1, e2) && areABTagsCompatible(e1, e2)) {
				return Optional.of(e2);
			}
		}
		return Optional.empty();
	}

	private boolean areABTagsCompatible(MatchmakingQueueEntry e1, MatchmakingQueueEntry e2) {
		return e1.getAbTag().equals(e2.getAbTag()) || (e1.getAbTagBattleCompatible() && e2.getAbTagBattleCompatible());
	}

	private void getNewPendingMatchesAndDisconnectDuplicatedPlayers() {
		List<T> newPendingMatchmakings = new ArrayList<>();
		queue.drainTo(newPendingMatchmakings);
		disconnectDuplicatedPlayers(pendingMatchmakings, newPendingMatchmakings);
		pendingMatchmakings.addAll(newPendingMatchmakings);
	}

	private void disconnectDuplicatedPlayers(List<T> pendingMatchmakings, List<T> newPendingMatchmakings) {
		for (T newEntry : newPendingMatchmakings) {
			Optional<T> oldEntry = pendingMatchmakings.stream().filter(x -> isSamePlayer(x, newEntry)).findFirst();
			oldEntry.ifPresent(x -> {
				logger.warn("Same player found in the queue: " + x.getPlayer().getUserId());
				x.notFound();
				pendingMatchmakings.remove(x);
			});
		}
	}

	private boolean isSamePlayer(MatchmakingQueueEntry e1, MatchmakingQueueEntry e2) {
		return e1.getPlayer().getUserId().equals(e2.getPlayer().getUserId());
	}

	private void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}