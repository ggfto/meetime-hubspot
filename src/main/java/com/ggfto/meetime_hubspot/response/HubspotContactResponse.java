package com.ggfto.meetime_hubspot.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubspotContactResponse {
	private String id;
    private Map<String, Object> properties;
    private String createdAt;
    private String updatedAt;
    private boolean archived;
}
