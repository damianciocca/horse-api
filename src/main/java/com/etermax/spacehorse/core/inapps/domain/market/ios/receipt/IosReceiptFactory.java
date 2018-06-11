package com.etermax.spacehorse.core.inapps.domain.market.ios.receipt;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.inapps.domain.market.ios.IosReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.ITunesStoreService;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.request.IosRequest;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response.IosReceiptInappResponse;
import com.etermax.spacehorse.core.inapps.domain.market.ios.receipt.itunes.response.IosResponse;
import com.etermax.spacehorse.core.inapps.error.InAppsErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class IosReceiptFactory {

	private static final Logger logger = LoggerFactory.getLogger(IosReceiptFactory.class);

	private static final int APPLE_SANDBOX_RECEIPT_CODE = 21007;
	private static final String APPLE_SANDBOX_URL = "https://sandbox.itunes.apple.com/";
	private static final String APPLE_PRODUCTION_URL = "https://buy.itunes.apple.com/";
	private static final int TIMEOUT_IN_SECONDS = 3;

	private ITunesStoreService productionService;
	private ITunesStoreService sandboxService;

	public IosReceiptFactory() {
		this.productionService = getAppleMarketService(APPLE_PRODUCTION_URL);
		this.sandboxService = getAppleMarketService(APPLE_SANDBOX_URL);
	}

	private static ITunesStoreService getAppleMarketService(String marketUrl) {
		final OkHttpClient httpClient = buildHttpClient();
		Retrofit retrofit = new Retrofit.Builder()//
				.addConverterFactory(JacksonConverterFactory.create())//
				.baseUrl(marketUrl)//
				.client(httpClient)//
				.build();
		return retrofit.create(ITunesStoreService.class);
	}

	public List<IosReceipt> buildIosReceipts(Object receipt) {

		String value = ((String) receipt);
		IosReceiptJson iosReceiptJson = getIosReceiptJson(value);
		Validate.notNull(iosReceiptJson);
		Validate.notNull(iosReceiptJson.payload);
		Validate.notNull(iosReceiptJson.transactionId);
		Validate.notNull(iosReceiptJson.store);

		IosResponse iosResponse = extractPayload(iosReceiptJson.payload);
		ArrayList<IosReceipt> iosReceipts = new ArrayList<>();

		for (IosReceiptInappResponse inappResponse : iosResponse.getReceipt().getInApps()) {
			IosReceipt iosReceipt = new IosReceipt(iosResponse.getReceipt(), inappResponse, iosReceiptJson.payload);
			iosReceipts.add(iosReceipt);
		}

		return iosReceipts;
	}

	private IosReceiptJson getIosReceiptJson(String value) {
		try {
			return new ObjectMapper().readValue(value, IosReceiptJson.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IosResponse extractPayload(String data) {
		IosResponse response = callItunesService(productionService, data);
		if (response.getStatus().equals(APPLE_SANDBOX_RECEIPT_CODE)) {
			return callItunesService(sandboxService, data);
		}
		return response;
	}

	private IosResponse callItunesService(ITunesStoreService service, String data) {
		try {
			Response<IosResponse> response = service.verifyReceipt(new IosRequest(data)).execute();

			if (!response.isSuccessful()) {
				logger.error("Error verifying receipt", response.errorBody());
				throw InAppsErrors.invalidReceipt();
			}

			return response.body();

		} catch (SocketTimeoutException ex) {
			logger.error("Error timeout verifying receipt", ex);
			throw InAppsErrors.serverTimeout();

		} catch (Exception ex) {
			logger.error("Error verifying receipt", ex);
			throw InAppsErrors.invalidReceipt();
		}
	}

	private static OkHttpClient buildHttpClient() {
		return new OkHttpClient()//
				.newBuilder()//
				.connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)//
				.readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).build();
	}
}
