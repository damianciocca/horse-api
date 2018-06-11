package com.etermax.spacehorse.core.login.model;

import com.etermax.spacehorse.core.error.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GooglePlayValidator {

	private static final Logger logger = LoggerFactory.getLogger(GooglePlayValidator.class);

	public static final long DEFAULT_ACCEPTABLE_TIME_SKEW_SECONDS = 365 * 24 * 60 * 60; // 1 year in seconds

	private static final JacksonFactory jsonFactory = new JacksonFactory();

	private static final String DEFAULT_WEB_CLIENT_ID = "136478556349-l2vc3fe99rupt8haa4hnqfo2nro48l40.apps.googleusercontent.com";

	private static final String DEFAULT_ANDROID_CLIENT_ID = "136478556349-qljg8j9o6ohv95lvn5gu263oa9kf2uf1.apps.googleusercontent.com";

	private String webClientId;

	private String androidClientId;

	public GooglePlayValidator(String webClientId, String androidClientId) {
		this.webClientId = webClientId;
		this.androidClientId = androidClientId;
	}

	public GooglePlayValidator() {
		this(DEFAULT_WEB_CLIENT_ID, DEFAULT_ANDROID_CLIENT_ID);
	}

	public String getGPGSIdWhenTokenIsVerified(String idTokenString) {
		return getGPGSIdWhenTokenIsVerified(idTokenString, DEFAULT_ACCEPTABLE_TIME_SKEW_SECONDS);
	}

	public String getGPGSIdWhenTokenIsVerified(String idTokenString, long acceptableTime) {
		NetHttpTransport transport = new NetHttpTransport();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(Collections.singletonList(webClientId))
				.setAcceptableTimeSkewSeconds(acceptableTime).build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            logger.error("Error verifying token: ", e);
            throw new InvalidTokenException("Invalid Token", e);
        }
        if (idToken == null) {
            throw new InvalidTokenException("Invalid Token");
        }

        Payload payload = idToken.getPayload();
		String googlePlayUserId = payload.getSubject();
		String payloadAuthorizedParty = payload.getAuthorizedParty();
		if (!payloadAuthorizedParty.equals(androidClientId)) {
			throw new InvalidTokenException("Invalid ANDROID_CLIENT_ID");
		}
		return googlePlayUserId;
	}

}
