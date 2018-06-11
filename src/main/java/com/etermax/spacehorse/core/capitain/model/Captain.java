package com.etermax.spacehorse.core.capitain.model;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.capitain.exceptions.UnlockingNotAllowedException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinAlreadyFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinNotBelongsToCaptainException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinNotFoundException;
import com.etermax.spacehorse.core.capitain.exceptions.skins.DuplicatedCaptainSlotException;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Captain {

	private static final Logger logger = LoggerFactory.getLogger(CaptainSlotsValidator.class);

	private final String captainId;
	private final CaptainDefinition captainDefinition;
	private Map<String, CaptainSkin> ownedSkinsById;
	private Map<Integer, CaptainSlot> captainSlots;

	public Captain(String captainId, CaptainDefinition captainDefinition, Map<String, CaptainSkin> ownedSkins,
			Map<Integer, CaptainSlot> captainSlots) {
		this.captainId = captainId;
		this.captainDefinition = captainDefinition;
		this.ownedSkinsById = ownedSkins;
		this.captainSlots = captainSlots;
	}

	public String getCaptainId() {
		return captainId;
	}

	public int getGemPrice() {
		return captainDefinition.getGemsPrice();
	}

	public int getGoldPrice() {
		return captainDefinition.getGoldPrice();
	}

	public Collection<CaptainSkin> getOwnedSkins() {
		return copyOf(ownedSkinsById.values());
	}

	public Collection<CaptainSlot> getCaptainSlots() {
		return copyOf(captainSlots.values());
	}

	public CaptainSlot getCaptainSlotBy(int slotNumber) {
		return captainSlots.get(slotNumber);
	}

	public void unlock(Player player) {
		if (maxMapNumberIsNotReached(player)) {
			String errorMessage = format("Captain ID [ %s ] can not be unlocked for player map number [ %s ]", getCaptainId(), player.getMapNumber());
			logger.error(errorMessage);
			throw new UnlockingNotAllowedException(errorMessage);
		}
	}

	public void addSkin(CaptainSkin captainSkin, Player player) {
		checkIfSkinWasAlreadyAdded(captainSkin, player);
		checkIfSkinBelongsToCaptain(captainSkin);
		ownedSkinsById.put(captainSkin.getCaptainSkinId(), captainSkin);
	}

	public void updateSkins(List<CaptainSkin> captainSkins) {
		if (captainSkins.isEmpty()) {
			captainSlots = clearSlotsOfSkins();
			return;
		}
		captainSkins.forEach(captainSkin -> {
			checkIfSkinWasPreviousAdded(captainSkin);
			checkIfSkinBelongsToCaptain(captainSkin);
		});
		this.captainSlots = addOrRemoveSlotsOfSkins(captainSkins);

	}

	public Map<String, CaptainSkin> getOwnedSkinsById() {
		return ImmutableMap.copyOf(ownedSkinsById);
	}

	private Map<Integer, CaptainSlot> clearSlotsOfSkins() {
		return Maps.newHashMap();
	}

	private Map<Integer, CaptainSlot> addOrRemoveSlotsOfSkins(List<CaptainSkin> captainSkins) {
		return captainSkins.stream().collect(Collectors
				.toMap(CaptainSkin::getSlotNumber, captainSkin -> new CaptainSlot(captainSkin.getSlotNumber(), captainSkin),
						throwExceptionIfSlotsAreDuplicated()));
	}

	private BinaryOperator<CaptainSlot> throwExceptionIfSlotsAreDuplicated() {
		return (v1, v2) -> {
			String errorMessage = "Duplicated slots were found when trying to update skins of captain.";
			logger.error(errorMessage);
			throw new DuplicatedCaptainSlotException(errorMessage);
		};
	}

	private void checkIfSkinWasPreviousAdded(CaptainSkin captainSkin) {
		if (skinIsNotFound(captainSkin.getCaptainSkinId())) {
			String errorMessage = format("Captain Skin ID [ %s ] not found in the owned captain skins ", captainSkin.getCaptainSkinId());
			logger.error(errorMessage);
			throw new CaptainSkinNotFoundException(errorMessage);
		}
	}

	private void checkIfSkinBelongsToCaptain(CaptainSkin captainSkin) {
		if (skinNotBelongsToCaptain(captainSkin)) {
			String errorMessage = format("Captain Skin ID [ %s ] not belongs to captain ID [ %s ] ", captainSkin.getCaptainSkinId(),
					captainDefinition.getId());
			logger.error(errorMessage);
			throw new CaptainSkinNotBelongsToCaptainException(errorMessage);
		}
	}

	private void checkIfSkinWasAlreadyAdded(CaptainSkin captainSkin, Player player) {
		if (skinIsFound(captainSkin.getCaptainSkinId())) {
			String errorMessage = format("Captain Skin ID [ %s ] already found in the captain for player ID [ %s ] ", captainSkin.getCaptainSkinId(),
					player.getUserId());
			logger.error(errorMessage);
			throw new CaptainSkinAlreadyFoundException(errorMessage);
		}
	}

	private boolean maxMapNumberIsNotReached(Player player) {
		return player.getMaxMapNumber() < captainDefinition.getMapNumber();
	}

	private boolean skinNotBelongsToCaptain(CaptainSkin captainSkin) {
		return !captainSkin.getCaptainId().equals(captainDefinition.getId());
	}

	private boolean skinIsFound(String skinId) {
		return ownedSkinsById.containsKey(skinId);
	}

	private boolean skinIsNotFound(String skinId) {
		return !skinIsFound(skinId);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	// Just for migration task. please DO NOT USE!!
	public void forceUpdateCaptainSlotsForMigrationTask(Map<Integer, CaptainSlot> captainSlots) {
		this.captainSlots = captainSlots;
	}

	// Just for migration task. please DO NOT USE!!
	public void forceUpdateOwnedCaptainsForMigrationTask(Map<String, CaptainSkin> ownedCaptainSkins) {
		this.ownedSkinsById = ownedCaptainSkins;
	}
}
