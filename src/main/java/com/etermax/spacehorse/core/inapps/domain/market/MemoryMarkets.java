package com.etermax.spacehorse.core.inapps.domain.market;

import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.user.model.Platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryMarkets implements Markets {

    private Map<Platform, Market> markets;

    public MemoryMarkets(){
        this.markets = new HashMap<>();
    }

    @Override
    public Optional<Market> findById(Platform platform) {
        return Optional.ofNullable(markets.get(platform));
    }

    public void registerMarket(Platform id, Market market){
        this.markets.put(id, market);
    }

}
