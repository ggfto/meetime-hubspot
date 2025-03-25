package com.ggfto.meetime_hubspot.model.dto;

import java.util.Map;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HubspotContactDTO {
	
	@NotBlank
	private String id;
	
	@Nonnull
	private Map<String, Object> properties;
}
