package com.etermax.spacehorse.core.specialoffer.model;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.exception.SpecialOfferNotFoundExpception;

public class SpecialOfferBoard {

	private final ServerTimeProvider timeProvider;
	private final ScheduledSpecialOfferValidator scheduledSpecialOfferValidator;
	private final NextRefreshTimeCalculator nextRefreshTimeCalculator;

	private DateTime nextRefreshServerTime;
	private List<SpecialOfferHistory> specialOffersHistory;
	private Map<String, SpecialOffer> specialOfferByIds;

	public SpecialOfferBoard(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
		this.specialOfferByIds = newHashMap();
		this.specialOffersHistory = newArrayList();
		this.scheduledSpecialOfferValidator = new ScheduledSpecialOfferValidator();
		this.nextRefreshTimeCalculator = new NextRefreshTimeCalculator(timeProvider);
		this.nextRefreshServerTime = nextRefreshTimeCalculator.defaultNextRefreshTime();
	}

	private SpecialOfferBoard(ServerTimeProvider timeProvider, Map<String, SpecialOffer> specialOfferByIds,
			List<SpecialOfferHistory> specialOffersHistory, DateTime nextRefreshServerTime) {
		this.timeProvider = timeProvider;
		this.specialOfferByIds = specialOfferByIds;
		this.specialOffersHistory = specialOffersHistory;
		this.scheduledSpecialOfferValidator = new ScheduledSpecialOfferValidator();
		this.nextRefreshTimeCalculator = new NextRefreshTimeCalculator(timeProvider);
		this.nextRefreshServerTime = nextRefreshServerTime;
	}

	public static SpecialOfferBoard restore(ServerTimeProvider timeProvider, Map<String, SpecialOffer> specialOfferByIds,
			List<SpecialOfferHistory> specialOfferHistories, DateTime nextRefreshServerTime) {
		return new SpecialOfferBoard(timeProvider, specialOfferByIds, specialOfferHistories, nextRefreshServerTime);
	}

	public void put(SpecialOffer specialOffer) {
		specialOfferByIds.put(specialOffer.getId(), specialOffer);
		updateSpecialOfferHistory(specialOffer);
	}

	public SpecialOffer get(String specialOfferId) {
		if (isNotExistInTheBoard(specialOfferId)) {
			throw new SpecialOfferNotFoundExpception(format("Special Offer ID [ %s ] not found in the board", specialOfferId));
		}
		return specialOfferByIds.get(specialOfferId);
	}

	public void consume(String specialOfferId) {
		if (isNotExistInTheBoard(specialOfferId)) {
			throw new SpecialOfferNotFoundExpception(format("Special Offer ID [ %s ] not found in the board", specialOfferId));
		}
		SpecialOffer specialOffer = specialOfferByIds.get(specialOfferId);
		specialOffer.consume();
		if (!specialOffer.hasAvailableAmount()) {
			specialOfferByIds.remove(specialOfferId);
		}
	}

	public void refresh(Player player, List<SpecialOfferDefinition> definitions) {
		removeExpiredSpecialOffers(definitions);
		putNewScheduledSpecialOffers(definitions, player);
		putNewOneTimeSpecialOffers(definitions, player);
		cleanRemovedSpecialOffers(definitions);
		updateNextRefreshServerTime(player, definitions);
	}

	public List<NextSpecialOffer> getNextSpecialOffers(Player player, List<SpecialOfferDefinition> definitions) {
		return getDefinitionsFilteredByScheduledAndPlayerMap(definitions, player)//
				.map(toSpecialOfferNextRefreshTime())//
				.collect(Collectors.toList());
	}

	public List<SpecialOffer> getOrderedSpecialOffers() {
		return copyOf(specialOfferByIds.values().stream().sorted(Comparator.comparingLong(SpecialOffer::getExpirationTimeInSeconds).reversed())
				.collect(Collectors.toList()));
	}

	public long getNexRefreshTimeInSeconds() {
		return ServerTime.fromDate(nextRefreshServerTime);
	}

	public List<SpecialOfferHistory> getSpecialOffersHistory() {
		return copyOf(specialOffersHistory);
	}

	private void removeExpiredSpecialOffers(List<SpecialOfferDefinition> specialOfferDefinitions) {
		specialOfferDefinitions.forEach(this::removeExpiredSpecialOffer);
	}

	private void putNewScheduledSpecialOffers(List<SpecialOfferDefinition> definitions, Player player) {
		getDefinitionsFilteredByScheduledAndPlayerMap(definitions, player)//
				.forEach(definition -> tryToPutANewScheduledSpecialOfferInTheBoard(definition, timeProvider.getDateTime(),
						definition.getActivationTime()));
	}

	private void putNewOneTimeSpecialOffers(List<SpecialOfferDefinition> definitions, Player player) {
		getDefinitionsFilteredByOneTimeAndPlayerMap(definitions, player).forEach(this::tryToPutANewOneTimeSpecialOfferInTheBoard);
	}

	private void cleanRemovedSpecialOffers(List<SpecialOfferDefinition> definitions) {
		List<String> specialOfferIdsToBeCleaned = specialOfferByIds.keySet().stream().filter(byNotExistSpecialOfferWith(definitions))
				.collect(Collectors.toList());
		specialOfferIdsToBeCleaned.forEach(this::forceRemove);
	}

	private void updateNextRefreshServerTime(Player player, List<SpecialOfferDefinition> definitions) {
		List<DateTime> nextRefreshTimes = getDefinitionsFilteredByScheduledAndPlayerMap(definitions, player)//
				.map(this::nextRefreshTimeFrom)//
				.collect(Collectors.toList());
		nextRefreshServerTime = nextRefreshTimeCalculator.minNextRefreshTimeOrDefault(nextRefreshTimes);
	}

	private void updateSpecialOfferHistory(SpecialOffer specialOffer) {
		specialOffersHistory.removeIf(alreadyExistsAnSpecialOfferHistoryFor(specialOffer));
		specialOffersHistory.add(new SpecialOfferHistory(specialOffer.getId(), specialOffer.getGroupId(), timeProvider.getDateTime()));
	}

	private void removeExpiredSpecialOffer(SpecialOfferDefinition definition) {
		if (existInTheBoardById(definition.getId())) {
			SpecialOffer specialOffer = get(definition.getId());
			tryToRemoveAnSpecialOfferIfIsExpired(specialOffer);
		}
	}

	private void forceRemove(String specialOfferId) {
		specialOfferByIds.remove(specialOfferId);
	}

	private void tryToPutANewScheduledSpecialOfferInTheBoard(SpecialOfferDefinition definition, DateTime now, DateTime activationTime) {
		if (specialOfferAlreadyExistsInTheBoard(definition)) {
			return;
		}

		if (scheduledSpecialOfferIsNotAvailable(definition, now, activationTime)) {
			return;
		}

		put(new SpecialOffer(definition, timeProvider));
	}

	private void tryToPutANewOneTimeSpecialOfferInTheBoard(SpecialOfferDefinition definition) {
		if (specialOfferAlreadyExistsInTheBoard(definition)) {
			return;
		}

		if (oneTimeSpecialOfferWasAlreadyShown(definition)) {
			return;
		}

		put(new SpecialOffer(definition, timeProvider));
	}

	private boolean oneTimeSpecialOfferWasAlreadyShown(SpecialOfferDefinition definition) {
		return getSpecialOffersHistory().stream().anyMatch(specialOfferHistory -> specialOfferHistory.getSpecialOfferId().equals(definition.getId()));
	}

	private boolean scheduledSpecialOfferIsNotAvailable(SpecialOfferDefinition definition, DateTime now, DateTime activationTime) {
		return !scheduledSpecialOfferValidator
				.isAvailable(now, activationTime, definition.getFrequencyInDays(), getSpecialOffersHistory(), definition.getId(),
						definition.getGroupId());
	}

	private boolean specialOfferAlreadyExistsInTheBoard(SpecialOfferDefinition definition) {
		return existInTheBoardById(definition.getId()) || existInTheBoardByGroupId(definition.getGroupId());

	}

	private void tryToRemoveAnSpecialOfferIfIsExpired(SpecialOffer specialOffer) {
		if (specialOffer.isExpired(timeProvider.getDateTime())) {
			forceRemove(specialOffer.getId());
		}
	}

	private boolean isNotExistInTheBoard(String specialOfferId) {
		return !existInTheBoardById(specialOfferId);
	}

	private boolean existInTheBoardByGroupId(String groupId) {
		if (isBlank(groupId))
			return false;
		return specialOfferByIds.values().stream().anyMatch(specialOffer -> groupId.equals(specialOffer.getGroupId()));
	}

	private boolean existInTheBoardById(String specialOfferId) {
		return specialOfferByIds.containsKey(specialOfferId);
	}

	private Stream<SpecialOfferDefinition> getDefinitionsFilteredByScheduledAndPlayerMap(List<SpecialOfferDefinition> specialOfferDefinitions,
			Player player) {
		return specialOfferDefinitions.stream() //
				.filter(SpecialOfferDefinition::isScheduledFilterEnabled) //
				.filter(byPlayerMapNumber(player.getMapNumber()));
	}

	private Stream<SpecialOfferDefinition> getDefinitionsFilteredByOneTimeAndPlayerMap(List<SpecialOfferDefinition> specialOfferDefinitions,
			Player player) {
		return specialOfferDefinitions.stream() //
				.filter(SpecialOfferDefinition::isOneTimeEnabled) //
				.filter(byPlayerMapNumber(player.getMapNumber()));
	}

	private Predicate<String> byNotExistSpecialOfferWith(List<SpecialOfferDefinition> specialOfferDefinitions) {
		return id -> specialOfferDefinitions.stream().noneMatch(definition -> Objects.equals(definition.getId(), id));
	}

	private Predicate<SpecialOfferDefinition> byPlayerMapNumber(int mapNumber) {
		return definition -> !definition.isMapFilterEnabled() || definition.getMapNumber() == mapNumber;
	}

	private Predicate<SpecialOfferHistory> alreadyExistsAnSpecialOfferHistoryFor(SpecialOffer specialOffer) {
		return specialOfferHistory -> specialOffer.getId().equalsIgnoreCase(specialOfferHistory.getSpecialOfferId());
	}

	private Function<SpecialOfferDefinition, NextSpecialOffer> toSpecialOfferNextRefreshTime() {
		return definition -> new NextSpecialOffer(new SpecialOffer(definition, timeProvider), nextRefreshTimeFrom(definition));
	}

	private DateTime nextRefreshTimeFrom(SpecialOfferDefinition definition) {
		return nextRefreshTimeCalculator.nextRefreshTime(timeProvider.getDateTime(), definition.getActivationTime(), definition.getFrequencyInDays());
	}

	@Override
	public String toString() {
		return "SpecialOfferBoard{" + "REFRESH TIME =" + nextRefreshServerTime + " , =========> CURRENT OFFERS =" + specialOfferByIds + ", "
				+ "=========>  " + "HISTORY =" + specialOffersHistory + '}';
	}

	public void cheatRemoveSpecialOffer(String specialOfferId) {
		specialOfferByIds.remove(specialOfferId);
	}
}
