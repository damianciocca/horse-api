package com.etermax.spacehorse.core.capitain.model;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.capitain.exceptions.CaptainAlreadyFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.CaptainNotFoundException;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.Maps;

public class CaptainsCollection {

	private static final Logger logger = LoggerFactory.getLogger(CaptainsCollection.class);

	private final String playerId;
	private final Map<String, Captain> ownedCaptainsById = Maps.newHashMap();
	private String selectedCaptainId;

	public CaptainsCollection(String playerId, List<Captain> ownedCaptains, String selectedCaptainId) {
		this.playerId = playerId;
		this.selectedCaptainId = selectedCaptainId;
		ownedCaptains.forEach(this::addCaptainAsOwn);
	}

	public static CaptainsCollection restore(String playerId, List<Captain> ownedCaptains, String selectedCaptainId) {
		return new CaptainsCollection(playerId, ownedCaptains, selectedCaptainId);
	}

	public void addCaptain(Player player, Captain captain) {
		if (captainIsFound(captain.getCaptainId())) {
			String errorMessage = format("Captain ID [ %s ] already found in the collection for player ID [ %s ] ", captain.getCaptainId(), playerId);
			logger.error(errorMessage);
			throw new CaptainAlreadyFoundException(errorMessage);
		}
		captain.unlock(player);
		addCaptainAsOwn(captain);
	}

	public void selectCaptain(String captainId) {
		if (captainIsNotFound(captainId)) {
			String errorMessage = format("Captain ID [ %s ] not found in the collection for player ID [ %s ] ", captainId, playerId);
			logger.error(errorMessage);
			throw new CaptainNotFoundException(errorMessage);
		}
		selectedCaptainId = captainId;
	}

	public Captain addCaptainSkin(Player player, String captainId, CaptainSkin captainSkin) {
		if (captainIsNotFound(captainId)) {
			String errorMessage = format("Captain ID [ %s ] not found in the collection for player ID [ %s ] ", captainId, playerId);
			logger.error(errorMessage);
			throw new CaptainNotFoundException(errorMessage);
		}
		Captain captain = ownedCaptainsById.get(captainId);
		captain.addSkin(captainSkin, player);
		return captain;
	}

	public Captain updateCaptainSkins(String captainId, List<CaptainSkin> captainSkins) {
		if (captainIsNotFound(captainId)) {
			String errorMessage = format("Captain ID [ %s ] not found in the collection for player ID [ %s ] ", captainId, playerId);
			logger.error(errorMessage);
			throw new CaptainNotFoundException(errorMessage);
		}
		Captain captain = ownedCaptainsById.get(captainId);
		captain.updateSkins(captainSkins);
		return captain;
	}

	public List<Captain> getCaptains() {
		return copyOf(ownedCaptainsById.values());
	}

	public String getSelectedCaptainId() {
		return selectedCaptainId;
	}

	public String getUserId() {
		return playerId;
	}

	public Collection<CaptainSlot> getCaptainSlotsOfSelectedCaptain() {
		return copyOf(getSelectedCaptain().getCaptainSlots());
	}

	public Captain getSelectedCaptain() {
		return ownedCaptainsById.get(getSelectedCaptainId());
	}

	private Captain addCaptainAsOwn(Captain captain) {
		return ownedCaptainsById.put(captain.getCaptainId(), captain);
	}

	private boolean captainIsNotFound(String captainId) {
		return !captainIsFound(captainId);
	}

	private boolean captainIsFound(String captainId) {
		return ownedCaptainsById.containsKey(captainId);

	}
}
