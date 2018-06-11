package com.etermax.spacehorse.core.club.infrastructure;

import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.club.ClubReporterConfiguration;
import com.etermax.spacehorse.core.club.request.UpdateUserScoreRequest;
import com.etermax.spacehorse.core.club.response.UpdateUserScoreResponse;
import com.etermax.spacehorse.core.login.resource.LoginResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubReporter {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginResource.class);

	private final ClubClient clubClient;
	private final ClubReporterConfiguration clubReporterConfiguration;

	public ClubReporter(ClubClient clubClient, ClubReporterConfiguration clubReporterConfiguration) {
		this.clubClient = clubClient;
		this.clubReporterConfiguration = clubReporterConfiguration;
	}

	public void updateUserScore(String userId, Integer newScore) {
		try{
			Call<UpdateUserScoreResponse> clubClientCall = clubClient
					.udpateUserScore(new UpdateUserScoreRequest(clubReporterConfiguration.getEnvironmentPrefix(), userId, newScore));

			clubClientCall.enqueue(new Callback<UpdateUserScoreResponse>() {
				@Override
				public void onResponse(Call<UpdateUserScoreResponse> call, Response<UpdateUserScoreResponse> response) {
					logger.debug("User score updated through Club Reporter", response.body().isSuccess(), response.body().getContent());
				}

				@Override
				public void onFailure(Call<UpdateUserScoreResponse> call, Throwable throwable) {
					logger.error("An exception occurred trying to update user score in Club Reporter.", throwable);
				}
			});

		} catch (Exception e) {
			logger.error("An exception occurred trying to update user score in Club Reporter.", e);
		}
	}
}
