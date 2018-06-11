package com.etermax.spacehorse.core.capitain.model;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.capitain.exceptions.skins.CaptainSkinNotBelongsToCaptainSlotException;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;

public class CaptainSlotsValidator {

	private static final Logger logger = LoggerFactory.getLogger(CaptainSlotsValidator.class);

	public void validateSlotOfSkins(String captainId, Map<Integer, String> skinIdsBySlots, List<CaptainSkin> captainSkins) {
		captainSkins.forEach(captainSkin -> {
			if (captainSkinsContainsAnySlotNumber(skinIdsBySlots, captainSkin)) {
				validateIfSlotNumberOfSkinIsValid(captainId, skinIdsBySlots, captainSkin);
			} else {
				String errorMessage = format("Skin ID [ %s ] from request does not belongs to any valid slot number from Captain ID [ %s ]",
						captainSkin.getCaptainSkinId(), captainId);
				logger.error(errorMessage);
				throw new CaptainSkinNotBelongsToCaptainSlotException(errorMessage);
			}
		});
	}

	private void validateIfSlotNumberOfSkinIsValid(String captainId, Map<Integer, String> skinIdsBySlots, CaptainSkin captainSkin) {
		String captainSkinIdFromSlot = skinIdsBySlots.get(captainSkin.getSlotNumber());
		if (captainSkinNotBelongsToTheSameSlotNumber(captainSkin, captainSkinIdFromSlot)) {
			String errorMessage = format("Skin ID [ %s ] from request does not belongs to slot number [ %s ] from Captain ID [ %s ]",
					captainSkinIdFromSlot, captainSkin.getSlotNumber(), captainId);
			logger.error(errorMessage);
			throw new CaptainSkinNotBelongsToCaptainSlotException(errorMessage);
		}
	}

	private boolean captainSkinNotBelongsToTheSameSlotNumber(CaptainSkin captainSkin, String captainSkinIdFromSlot) {
		return !captainSkin.getCaptainSkinId().equals(captainSkinIdFromSlot);
	}

	private boolean captainSkinsContainsAnySlotNumber(Map<Integer, String> skinIdsBySlots, CaptainSkin captainSkin) {
		return skinIdsBySlots.containsKey(captainSkin.getSlotNumber());
	}
}
