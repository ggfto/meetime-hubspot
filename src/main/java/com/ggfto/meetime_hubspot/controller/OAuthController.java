package com.ggfto.meetime_hubspot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ggfto.meetime_hubspot.response.HubspotOAuthResponse;
import com.ggfto.meetime_hubspot.service.oauth.IHubspotOAuthService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {
	private final IHubspotOAuthService hubspotOAuthService;

	@Hidden
	@Operation(summary = "Recupera acesso em callback", description = "Recupera acesso em callback e retorna o token. Endpoint ocultado na documentação.")
	@ApiResponse(responseCode = "200", description = "Token gerado com sucesso")
	@ApiResponse(responseCode = "400", description = "Código inválido ou erro na autenticação")
	@GetMapping("/callback")
	@ResponseBody
	public ResponseEntity<HubspotOAuthResponse> callback(@RequestParam("code") String code) {
		try {
            log.info("Recebendo callback de autenticação do HubSpot com código: {}", code);
            HubspotOAuthResponse tokenResponse = hubspotOAuthService.callback(code);
            return ResponseEntity.ok(tokenResponse);
        } catch (IllegalArgumentException e) {
            log.warn("Código inválido recebido no callback: {}", code, e);
            return ResponseEntity.badRequest().body(new HubspotOAuthResponse("Código inválido."));
        } catch (Exception e) {
            log.error("Erro inesperado ao processar o callback do HubSpot", e);
            return ResponseEntity.badRequest().body(new HubspotOAuthResponse("Erro ao processar a autenticação."));
        }
	}
}
