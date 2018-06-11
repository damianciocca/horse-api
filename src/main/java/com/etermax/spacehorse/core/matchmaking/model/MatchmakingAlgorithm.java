package com.etermax.spacehorse.core.matchmaking.model;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;

public class MatchmakingAlgorithm {

    public MatchmakingAlgorithm() {
    }

    public boolean invokeWith(MatchmakingAlgorithmConfiguration configuration, PlayerWinRate playerWRA, PlayerWinRate playerWRB) {
        // TODO: ADD WIN LOSE RATE IN FOLLOWING ITERATION
        double mmrIndex = calculateMmrIndex(configuration.getMmrLimit(), playerWRA.getMmr(), playerWRB.getMmr());
        //double wlrIndex = calculateWLRateIndex(playerWRA.getWinRate(), playerWRB.getWinRate());
        double index =  mmrIndex;//* wlrIndex;

        return index > configuration.getIndexTolerance();
    }

    private Double calculateWLRateIndex(Double wlRateA, Double wlRateB) {
        double diffWLRate = Math.abs(wlRateA - wlRateB);
        return Math.pow(1 - diffWLRate, 2);
    }

    private Double calculateMmrIndex(int mmrLimit, Integer mmrA, Integer mmrB) {
        int doubleLimit = mmrLimit * 2;
        int abs = Math.abs(mmrA - mmrB);
        int diffOfMmr = abs > doubleLimit ? doubleLimit : abs;
        return Math.pow(1 - (double) diffOfMmr / (double) doubleLimit, 2);
    }

}
