package com.ggfto.meetime_hubspot.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggfto.meetime_hubspot.service.auth.IHubspotAuthService;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final IHubspotAuthService hubspotAuthService;

	@Operation(
	        summary = "Realizar login na plataforma HubSpot",
	        description = "Direciona para a plataforma HubSpot para login",
	        externalDocs = @ExternalDocumentation(
	            description = "Essa URL deve ser acessada pelo navegador para retorno do callback",
	            url = "http://localhost:8080/v1/auth/login"
	        )
	    )
	    @ApiResponses({
	        @ApiResponse(responseCode = "302", description = "Redireciona para o HubSpot para autenticação"),
	        @ApiResponse(responseCode = "500", description = "Erro ao tentar gerar a URL de login")
	    })
    @GetMapping("/login")
	 public void loginHubSpot(HttpServletResponse response) throws IOException {
        hubspotAuthService.loginHubSpot(response);
	}
}
