package com.etermax.spacehorse.core.club.infrastructure;

import java.util.concurrent.TimeUnit;

import com.etermax.spacehorse.core.club.ClubReporterConfiguration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class ClubReporterFactory {

	public static ClubClient create(ClubReporterConfiguration clubReporterConfiguration) {
		OkHttpClient okHttpClient = buildHttpClient(clubReporterConfiguration);
		String serverUrl = clubReporterConfiguration.getServerUrl();
		String serverUrlWithGameCodeByEnvironment = serverUrl.concat(clubReporterConfiguration.getEnvironmentPrefix()).concat("/");
		Retrofit retrofit = (new Retrofit.Builder()).baseUrl(serverUrlWithGameCodeByEnvironment).client(okHttpClient).addConverterFactory(
				JacksonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
		return retrofit.create(ClubClient.class);
	}

	private static OkHttpClient buildHttpClient(ClubReporterConfiguration clubReporterConfiguration) {
		int readTimeoutInSeconds = clubReporterConfiguration.getReadTimeoutInSeconds();
		int connectTimeoutInSeconds = clubReporterConfiguration.getConnectTimeoutInSeconds();
		int writeTimeoutInSeconds = clubReporterConfiguration.getWriteTimeoutInSeconds();
		return (new okhttp3.OkHttpClient.Builder())
				.readTimeout((long)readTimeoutInSeconds, TimeUnit.SECONDS)
				.connectTimeout((long)connectTimeoutInSeconds, TimeUnit.SECONDS)
				.writeTimeout((long)writeTimeoutInSeconds, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true)
				.build();
	}
}
