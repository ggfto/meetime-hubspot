package com.ggfto.meetime_hubspot.utils;

import org.springframework.http.ResponseEntity;

import com.ggfto.meetime_hubspot.response.HubspotContactListResponse;

@FunctionalInterface
public interface RequestExecutor {
	ResponseEntity<HubspotContactListResponse> execute();
}
