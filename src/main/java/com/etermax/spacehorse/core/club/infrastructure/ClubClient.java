package com.etermax.spacehorse.core.club.infrastructure;

import com.etermax.spacehorse.core.club.request.UpdateUserScoreRequest;
import com.etermax.spacehorse.core.club.response.UpdateUserScoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClubClient {
	@POST("/rest/user/score")
	Call<UpdateUserScoreResponse> udpateUserScore(@Body UpdateUserScoreRequest request);
}
