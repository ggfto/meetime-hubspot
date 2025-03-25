package com.ggfto.meetime_hubspot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggfto.meetime_hubspot.model.dto.HubspotContactDTO;
import com.ggfto.meetime_hubspot.response.HubspotContactListResponse;
import com.ggfto.meetime_hubspot.response.HubspotContactResponse;
import com.ggfto.meetime_hubspot.service.contact.IHubspotContactService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/contacts")
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Integração HubSpot", description = "Serviço responsável pela integração com o HubSpot"))
public class ContactController {

	private final IHubspotContactService contactService;

	@Operation(summary = "Criar um contato no HubSpot", description = "Cria um novo contato na plataforma HubSpot.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@PostMapping
	public ResponseEntity<HubspotContactResponse> createContact(@Valid @RequestBody HubspotContactDTO contactDTO) {
		HubspotContactResponse response = contactService.createContact(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar contatos do HubSpot", description = "Obtém a lista de contatos do HubSpot.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de contatos recuperada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@GetMapping
	public ResponseEntity<HubspotContactListResponse> getContacts() {
		 HubspotContactListResponse response = contactService.getContacts();
	        return ResponseEntity.ok(response);
	}
}
