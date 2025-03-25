package com.ggfto.meetime_hubspot.service.auth;

import jakarta.servlet.http.HttpServletResponse;

public interface IHubspotAuthService {
	void loginHubSpot(HttpServletResponse response);
}
