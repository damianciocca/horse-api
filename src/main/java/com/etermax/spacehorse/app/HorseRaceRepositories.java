package com.etermax.spacehorse.app;

import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class HorseRaceRepositories {

    public static UserRepository userRepository;
    public static CatalogRepository catalogRepository;
    public static PlayerRepository playerRepository;
    public static BattleRepository battleRepository;
    public static PlayerWinRateRepository playerWinRateRepository;
    public static AuthRepository authRepository;
    public static MarketRepository marketRepository;
    private ServerSettingsRepository serverSettingsRepository;
    private QuestBoardRepository questBoardRepository;
    private SpecialOfferBoardRepository specialOfferBoardRepository;
    private CaptainCollectionRepository captainCollectionRepository;
    private QuotaVideoRewardRepository quotaVideoRewardRepository;
    private AchievementCollectionRepository achievementCollectionRepository;
    private PlayerLeagueRepository playerLeagueRepository;

    public HorseRaceRepositories() {
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        HorseRaceRepositories.userRepository = userRepository;
    }

    public CatalogRepository getCatalogRepository() {
        return catalogRepository;
    }

    public void setCatalogRepository(CatalogRepository catalogRepository) {
        HorseRaceRepositories.catalogRepository = catalogRepository;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public void setPlayerRepository(PlayerRepository playerRepository) {
        HorseRaceRepositories.playerRepository = playerRepository;
    }

    public BattleRepository getBattleRepository() {
        return battleRepository;
    }

    public void setBattleRepository(BattleRepository battleRepository) {
        HorseRaceRepositories.battleRepository = battleRepository;
    }

    public PlayerWinRateRepository getPlayerWinRateRepository() {
        return HorseRaceRepositories.playerWinRateRepository;
    }

    public void setPlayerWinRateRepository(PlayerWinRateRepository playerWinRateRepository) {
        HorseRaceRepositories.playerWinRateRepository = playerWinRateRepository;
    }

    public AuthRepository getAuthRepository() {
        return HorseRaceRepositories.authRepository;
    }

    public void setAuthRepository(AuthRepository authRepository){
        HorseRaceRepositories.authRepository = authRepository;
    }

    public MarketRepository getMarketRepository() {
        return marketRepository;
    }

    public void setMarketRepository(MarketRepository marketRepository) {
        HorseRaceRepositories.marketRepository = marketRepository;
    }

    public void setServerSettings(ServerSettingsRepository serverSettingsRepository) {
        this.serverSettingsRepository = serverSettingsRepository;
    }

    public ServerSettingsRepository getServerSettingsRepository() {
        return serverSettingsRepository;
    }

    public void setQuestBoardRepository(QuestBoardRepository questBoardRepository) {
        this.questBoardRepository = questBoardRepository;
    }

    public QuestBoardRepository getQuestBoardRepository() {
		return questBoardRepository;
	}

    public void setSpecialOfferBoardRepository(SpecialOfferBoardRepository specialOfferBoardRepository) {
        this.specialOfferBoardRepository = specialOfferBoardRepository;
    }

    public SpecialOfferBoardRepository getSpecialOfferBoardRepository() {
        return specialOfferBoardRepository;
    }

    public CaptainCollectionRepository getCaptainCollectionRepository() {
        return captainCollectionRepository;
    }

    public void setCaptainCollectionRepository(CaptainCollectionRepository captainCollectionRepository) {
        this.captainCollectionRepository = captainCollectionRepository;
    }

    public QuotaVideoRewardRepository getQuotaVideoRewardRepository() {
        return quotaVideoRewardRepository;
    }

    public void setQuotaVideoRewardRepository(QuotaVideoRewardRepository quotaVideoRewardRepository) {
        this.quotaVideoRewardRepository = quotaVideoRewardRepository;
    }

    public AchievementCollectionRepository getAchievementCollectionRepository() {
        return achievementCollectionRepository;
    }

    public void setAchievementCollectionRepository(AchievementCollectionRepository achievementCollectionRepository) {
        this.achievementCollectionRepository = achievementCollectionRepository;
    }

    public PlayerLeagueRepository getPlayerLeagueRepository() {
        return playerLeagueRepository;
    }

    public void setPlayerLeagueRepository(PlayerLeagueRepository playerLeagueRepository) {
        this.playerLeagueRepository = playerLeagueRepository;
    }
}
