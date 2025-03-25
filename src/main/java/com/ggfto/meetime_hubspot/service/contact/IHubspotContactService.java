package com.ggfto.meetime_hubspot.service.contact;

import com.ggfto.meetime_hubspot.model.dto.HubspotContactDTO;
import com.ggfto.meetime_hubspot.response.HubspotContactListResponse;
import com.ggfto.meetime_hubspot.response.HubspotContactResponse;

public interface IHubspotContactService {
	public HubspotContactResponse createContact(HubspotContactDTO contactDTO);
	public HubspotContactListResponse getContacts();
}
