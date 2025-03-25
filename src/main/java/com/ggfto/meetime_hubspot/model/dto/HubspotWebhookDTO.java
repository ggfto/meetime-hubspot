package com.ggfto.meetime_hubspot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HubspotWebhookDTO {
	private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long appId;
    private Long occurredAt;
    private String subscriptionType;
    private int attemptNumber;
    private Long objectId;
    private String changeFlag;
    private String changeSource;
    private String sourceId;
}
