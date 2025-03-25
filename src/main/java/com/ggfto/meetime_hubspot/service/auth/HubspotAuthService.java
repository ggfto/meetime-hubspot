package com.ggfto.meetime_hubspot.service.auth;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.ggfto.meetime_hubspot.exception.HubspotAuthException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubspotAuthService implements IHubspotAuthService {

	 @Value("${hubspot.client-id}")
	    private String clientId;

	    @Value("${hubspot.redirect-uri}")
	    private String redirectUri;
	    
	    @Value("${hubspot.auth-uri}")
	    private String hubspotAuthUri;

	    private static final String SCOPES = "crm.objects.contacts.read crm.objects.contacts.write crm.schemas.contacts.write";
	    
	    @Override
	    public void loginHubSpot(HttpServletResponse response) {
	        validarConfiguracoes();

	        try {
	            String authUrl = UriComponentsBuilder.fromUri(URI.create(hubspotAuthUri))
	                    .queryParam("client_id", clientId)
	                    .queryParam("redirect_uri", redirectUri)
	                    .queryParam("scope", SCOPES)
	                    .queryParam("response_type", "code")
	                    .toUriString();

	            log.info("Redirecionando para autenticação do HubSpot: {}", authUrl);
	            response.sendRedirect(authUrl);
	        } catch (IOException e) {
	            log.error("Erro ao redirecionar para autenticação do HubSpot", e);
	            throw new HubspotAuthException("Falha ao redirecionar para autenticação do HubSpot", e);
	        }
	    }

	    private void validarConfiguracoes() {
	        if ((clientId == null) || clientId.isBlank()) {
	            throw new HubspotAuthException("Configuração ausente: 'hubspot.client-id'");
	        }
	        if ((redirectUri == null) || redirectUri.isBlank()) {
	            throw new HubspotAuthException("Configuração ausente: 'hubspot.redirect-uri'");
	        }
	        if ((hubspotAuthUri == null) || hubspotAuthUri.isBlank()) {
	            throw new HubspotAuthException("Configuração ausente: 'hubspot.auth-uri'");
	        }
	    }

}
