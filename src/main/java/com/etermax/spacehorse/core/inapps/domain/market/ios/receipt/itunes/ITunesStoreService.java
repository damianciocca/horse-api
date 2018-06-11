package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes;

import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.request.IosRequest;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response.IosResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ITunesStoreService {

    @POST("verifyReceipt")
    Call<IosResponse> verifyReceipt(@Body IosRequest request);

}
