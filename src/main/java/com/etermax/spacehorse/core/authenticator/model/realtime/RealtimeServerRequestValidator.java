package com.etermax.spacehorse.core.authenticator.model.realtime;

import javax.servlet.http.HttpServletRequest;

public interface RealtimeServerRequestValidator {
	boolean validateRequest(HttpServletRequest request);
}
