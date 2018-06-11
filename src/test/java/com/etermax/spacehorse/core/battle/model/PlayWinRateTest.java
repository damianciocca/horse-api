package com.etermax.spacehorse.core.battle.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayWinRateTest {

    @Test
    public void testGetWinRateLimit() {
        String userId = "userId";
        PlayerWinRate playerWinRate = new PlayerWinRate(userId);

        assertThat(playerWinRate.getWinRate()).isEqualTo(0);
    }

    @Test
    public void testGetWinRateNormal() {
        String userId = "userId";
        PlayerWinRate playerWinRate = new PlayerWinRate(userId, 50, 50, 1);

        assertThat(playerWinRate.getWinRate()).isEqualTo(0.5);
    }

}
