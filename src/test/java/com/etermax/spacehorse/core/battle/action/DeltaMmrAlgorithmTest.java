package com.etermax.spacehorse.core.battle.action;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.DeltaMmrAlgorithm;
import com.etermax.spacehorse.core.battle.model.DeltaMmrPercentage;
import com.etermax.spacehorse.core.battle.model.DeltaMmrPercentageSelector;
import com.etermax.spacehorse.core.battle.model.PlayersDeltaMmr;
import com.google.common.collect.Lists;

public class DeltaMmrAlgorithmTest {

    private static final int MINIMUM_MMR = 100;
    private DeltaMmrAlgorithm deltaMmrAlgorithm;
    private DeltaMmrPercentageSelector deltaMmrPercentageSelector;
    private PlayersDeltaMmr playersDeltaMmr;

    @Test
    public void whenWinnerHas1185mmrAndLoser1121mmrThenWinnerObtain19Mmr(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(1185, 1121);

        thenMmrOfPlayersIs(19, -19);
    }

    @Test
    public void whenWinnerHas1121mmrAndLoser1185mmrThenWinnerObtain41Mmr(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(1121,1185);

        thenMmrOfPlayersIs(41, -41);
    }

    @Test
    public void whenWinnerHas1697mmrAndLoser1683mmrThenWinnerObtain28Mmr(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(1697,1683);

        thenMmrOfPlayersIs(28, -28);
    }

   @Test
    public void whenWinnerHas1683mmrAndLoser1697mmrThenWinnerObtain32Mmr(){
       givenAMmrDeltaPercentageSelector();
       givenAMmrAlgorithm();

        whenCalculateDeltaMmr(1683,1697);

        thenMmrOfPlayersIs(32, -32);
    }

    @Test
    public void whenWinnerHas1841mmrAndLoser1841mmrThenWinnerObtain30Mmr(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(1841,1841);

        thenMmrOfPlayersIs(30, -30);
    }

    @Test
    public void whenWinnerHas0mmrAndLoser300mmrThenWinnerObtain55mmrAndLoserDiscount27(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(0,300);

        thenMmrOfPlayersIs(55, -27);
    }

    @Test
    public void whenWinnerHas300mmrAndLoser0mmrThenWinnerObtain5MmrAndLoser0(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(300,0);

        thenMmrOfPlayersIs(5, 0);
    }

    @Test
    public void whenWinnerHas101mmrAndLoser101mmrThenWinnerObtain30MmrAndLoser0(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(101,101);

        thenMmrOfPlayersIs(30, 0);
    }

    @Test
    public void whenWinnerHas0mmrAndLoser0mmrThenWinnerObtain30MmrAndLoser0(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(0,0);

        thenMmrOfPlayersIs(30, 0);
    }

    @Test
    public void whenWinnerHas102mmrAndLoser102mmrThenWinnerObtain30MmrAndLoserDiscount1(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(102,102);

        thenMmrOfPlayersIs(30, -1);
    }

    @Test
    public void whenWinnerHas300mmrAndLoser700mmrThenWinnerObtain55MmrAndLoserDiscount41(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(300,700);

        thenMmrOfPlayersIs(55, -41);
    }

    @Test
    public void whenWinnerHas700mmrAndLoser999mmrThenWinnerObtain55MmrAndLoserDiscount49(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(700,999);

        thenMmrOfPlayersIs(55, -49);
    }

    @Test
    public void whenWinnerHas700mmrAndLoser1000mmrThenWinnerObtain55MmrAndLoserDiscount55(){
        givenAMmrDeltaPercentageSelector();
        givenAMmrAlgorithm();

        whenCalculateDeltaMmr(700,1000);

        thenMmrOfPlayersIs(55, -55);
    }

    private void thenMmrOfPlayersIs(int mmrWinnerExpected, int mmrLoserExpected) {
        Integer mmrWinner = playersDeltaMmr.getWinnerDeltaMmr();
        Integer mmrLoser = playersDeltaMmr.getLoserDeltaMmr();
        assertThat(mmrWinner).isEqualTo(mmrWinnerExpected);
        assertThat(mmrLoser).isEqualTo(mmrLoserExpected);
    }

    private void whenCalculateDeltaMmr(int mmrOfWinner, int mmrOfLoser) {
        playersDeltaMmr = deltaMmrAlgorithm.calculate(mmrOfWinner, mmrOfLoser);
    }

    private void givenAMmrDeltaPercentageSelector() {
        DeltaMmrPercentage deltaMmrPercentage1 = new DeltaMmrPercentage(100, 300, 100, 50);
        DeltaMmrPercentage deltaMmrPercentage2 = new DeltaMmrPercentage(300, 700, 100, 75);
        DeltaMmrPercentage deltaMmrPercentage3 = new DeltaMmrPercentage(700, 999, 100, 90);

        deltaMmrPercentageSelector = new DeltaMmrPercentageSelector(
                Lists.newArrayList(deltaMmrPercentage1, deltaMmrPercentage2, deltaMmrPercentage3));
    }

    private void givenAMmrAlgorithm() {
        deltaMmrAlgorithm = new DeltaMmrAlgorithm(30, 6.0, deltaMmrPercentageSelector, MINIMUM_MMR);
    }
}
