package com.etermax.spacehorse.core.specialoffer.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferHistory;
import com.google.common.collect.Maps;

@DynamoDBTable(tableName = "specialOfferBoard")
public class DynamoSpecialOfferBoard implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "specialOffers")
	private List<DynamoSpecialOffer> specialOffers;

	@DynamoDBAttribute(attributeName = "refreshTimeInSeconds")
	private long refreshTimeInSeconds;

	@DynamoDBAttribute(attributeName = "specialOffersHistory")
	private List<DynamoSpecialOfferHistory> specialOffersHistory;

	public DynamoSpecialOfferBoard() {
		// just for dynamo mapper
	}

	private DynamoSpecialOfferBoard(String userId, List<DynamoSpecialOffer> specialOffers, long refreshTimeInSeconds,
			List<DynamoSpecialOfferHistory> specialOffersHistory) {
		this.userId = userId;
		this.specialOffers = specialOffers;
		this.refreshTimeInSeconds = refreshTimeInSeconds;
		this.specialOffersHistory = specialOffersHistory;
	}

	public static DynamoSpecialOfferBoard mapFromSpecialOfferBoard(String userId, SpecialOfferBoard specialOfferBoard) {
		List<DynamoSpecialOffer> dynamoSpecialOffers = specialOfferBoard.getOrderedSpecialOffers().stream().map(toDynamoSpecialOffer())
				.collect(Collectors.toList());
		List<DynamoSpecialOfferHistory> dynamoSpecialOfferHistories = specialOfferBoard.getSpecialOffersHistory().stream()
				.map(toDynamoSpecialOffersHistory()).collect(Collectors.toList());
		return new DynamoSpecialOfferBoard(userId, dynamoSpecialOffers, specialOfferBoard.getNexRefreshTimeInSeconds(), dynamoSpecialOfferHistories);
	}

	public static SpecialOfferBoard mapFromDynamoSpecialOfferBoard(DynamoSpecialOfferBoard dynamoSpecialOfferBoard, ServerTimeProvider timeProvider,
			CatalogEntriesCollection<SpecialOfferDefinition> specialOfferDefinitions) {
		Map<String, SpecialOffer> specialOffers = restoreSpecialOffersFrom(dynamoSpecialOfferBoard, specialOfferDefinitions);
		List<SpecialOfferHistory> specialOfferHistories = dynamoSpecialOfferBoard.getSpecialOffersHistory().stream().map(toSpecialOffersHistory())
				.collect(Collectors.toList());
		DateTime nextRefreshServerTime = ServerTime.toDateTime(dynamoSpecialOfferBoard.getRefreshTimeInSeconds());
		return SpecialOfferBoard.restore(timeProvider, specialOffers, specialOfferHistories, nextRefreshServerTime);
	}

	private static Map<String, SpecialOffer> restoreSpecialOffersFrom(DynamoSpecialOfferBoard dynamoSpecialOfferBoard,
			CatalogEntriesCollection<SpecialOfferDefinition> specialOfferDefinitions) {
		Map<String, SpecialOffer> specialOffers = Maps.newConcurrentMap();
		dynamoSpecialOfferBoard.getSpecialOffers().forEach(dynamoSpecialOffer -> {
			specialOfferDefinitions.findById(dynamoSpecialOffer.getId()).map(toSpecialOffer(dynamoSpecialOffer))
					.ifPresent(putOfferInBoard(specialOffers));
		});
		return specialOffers;
	}

	private static Consumer<SpecialOffer> putOfferInBoard(Map<String, SpecialOffer> specialOffers) {
		return specialOffer -> specialOffers.put(specialOffer.getId(), specialOffer);
	}

	private static Function<SpecialOfferDefinition, SpecialOffer> toSpecialOffer(DynamoSpecialOffer dynamoSpecialOffer) {
		return definition -> SpecialOffer.restore(definition, ServerTime.toDateTime(dynamoSpecialOffer.getExpirationTimeInSeconds()),
				dynamoSpecialOffer.getAvailableAmountUntilExpiration());
	}

	private static Function<DynamoSpecialOfferHistory, SpecialOfferHistory> toSpecialOffersHistory() {
		return dynamoSpecialOfferHistory -> SpecialOfferHistory.restore(dynamoSpecialOfferHistory.getId(), dynamoSpecialOfferHistory.getGroupId(),
				dynamoSpecialOfferHistory.getCreationTimeInSeconds());
	}

	private static Function<SpecialOffer, DynamoSpecialOffer> toDynamoSpecialOffer() {
		return specialOffer -> new DynamoSpecialOffer(specialOffer.getId(), specialOffer.getExpirationTimeInSeconds(),
				specialOffer.getAvailableAmountUntilExpiration());
	}

	private static Function<SpecialOfferHistory, DynamoSpecialOfferHistory> toDynamoSpecialOffersHistory() {
		return specialOfferHistory -> new DynamoSpecialOfferHistory(specialOfferHistory.getSpecialOfferId(), specialOfferHistory.getGroupId(),
				ServerTime.fromDate(specialOfferHistory.getCreationTime()));
	}

	@Override
	public String getId() {
		return getUserId();
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<DynamoSpecialOffer> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(List<DynamoSpecialOffer> specialOffers) {
		this.specialOffers = specialOffers;
	}

	public long getRefreshTimeInSeconds() {
		return refreshTimeInSeconds;
	}

	public void setRefreshTimeInSeconds(long refreshTimeInSeconds) {
		this.refreshTimeInSeconds = refreshTimeInSeconds;
	}

	public List<DynamoSpecialOfferHistory> getSpecialOffersHistory() {
		return specialOffersHistory;
	}

	public void setSpecialOffersHistory(List<DynamoSpecialOfferHistory> specialOffersHistory) {
		this.specialOffersHistory = specialOffersHistory;
	}
}
