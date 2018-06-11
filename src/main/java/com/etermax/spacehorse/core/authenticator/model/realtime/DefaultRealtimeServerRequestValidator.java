package com.etermax.spacehorse.core.authenticator.model.realtime;

import javax.servlet.http.HttpServletRequest;

public class DefaultRealtimeServerRequestValidator implements  RealtimeServerRequestValidator {

	public static final String REALTIME_API_KEY_HEADER = "Realtime-Api-Key";
	public static final String REALTIME_API_KEY_VALUE = "3XK4VTPBAsBrfkftzx1fgNHo9n3O4gwL";

	public boolean validateRequest(HttpServletRequest request) {
		String key = request.getHeader(REALTIME_API_KEY_HEADER);
		return key != null && key.equals(REALTIME_API_KEY_VALUE);
	}
}
