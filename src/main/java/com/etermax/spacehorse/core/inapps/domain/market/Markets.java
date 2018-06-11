package com.etermax.spacehorse.core.inapps.domain.market;

import com.etermax.spacehorse.core.inapps.domain.Market;
import com.etermax.spacehorse.core.user.model.Platform;

import java.util.Optional;

public interface Markets {

    Optional<Market> findById(Platform platform);

}
