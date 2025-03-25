package com.ggfto.meetime_hubspot.service.oauth;

import com.ggfto.meetime_hubspot.exception.HubspotOAuthException;
import com.ggfto.meetime_hubspot.response.HubspotOAuthResponse;

public interface IHubspotOAuthService {
	HubspotOAuthResponse callback(String code) throws HubspotOAuthException;
}
