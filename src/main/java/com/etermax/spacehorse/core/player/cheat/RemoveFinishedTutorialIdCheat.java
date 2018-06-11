package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class RemoveFinishedTutorialIdCheat extends Cheat {

    @Override
    public String getCheatId() {
        return "removeFinishedTutorial";
    }

    @Override
    public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
        String tutorialId = getParameterString(parameters, 0);
        player.removeFinishedTutorialId(tutorialId);
        return new CheatResponse(tutorialId);
    }
}
