package com.ggfto.meetime_hubspot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggfto.meetime_hubspot.model.dto.HubspotWebhookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/webhooks")
@RequiredArgsConstructor
public class WebHookController {
	
	@Operation(summary = "Recebe dados de criação de contato do HubSpot", description = "Recebe os dados de contato criados no HubSpot.")
	@ApiResponse(responseCode = "200", description = "Lista de contatos recuperada com sucesso")
	@PostMapping("/contact-creation")
	public ResponseEntity<Void> contactCreation(@Valid @RequestBody List<HubspotWebhookDTO> data) {
		log.info(data.toString());
		return ResponseEntity.noContent().build();
	}
}
