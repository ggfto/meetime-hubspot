package com.ggfto.meetime_hubspot.service.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ggfto.meetime_hubspot.exception.HubspotOAuthException;
import com.ggfto.meetime_hubspot.model.Token;
import com.ggfto.meetime_hubspot.response.HubspotOAuthResponse;
import com.ggfto.meetime_hubspot.service.token.ITokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubspotOAuthService implements IHubspotOAuthService {
	
	@Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.client-secret}")
    private String clientSecret;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.api-uri}")
    private String hubspotApiUri;
    
    @Value("${hubspot.token-endpoint}")
    private String tokenEndpoint;
    
    private final RestTemplate restTemplate;
    
    private final ITokenService tokenService;

	@Override
	public HubspotOAuthResponse callback(String code) throws HubspotOAuthException {
		MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("redirect_uri", redirectUri);
        data.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data, headers);
        
        try {
            log.info("Enviando requisição para obter token do HubSpot...");

            ResponseEntity<Token> response = restTemplate.exchange(hubspotApiUri + tokenEndpoint, HttpMethod.POST, entity, Token.class);

            if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Resposta inválida da API do HubSpot: status {}", response != null ? response.getStatusCode() : null);
                throw new HubspotOAuthException("Falha ao obter token do HubSpot.");
            }

            tokenService.setToken(response.getBody());
            log.info("Token salvo com sucesso.");

            return new HubspotOAuthResponse("Login realizado com sucesso.");
        } catch (HttpClientErrorException e) {
            log.warn("Erro de cliente ao autenticar no HubSpot: {}", e.getStatusCode(), e);
            throw new HubspotOAuthException("Erro na autenticação. Verifique o código informado.");
        } catch (HttpServerErrorException e) {
            log.error("Erro no servidor do HubSpot ao autenticar.", e);
            throw new HubspotOAuthException("Erro interno no serviço do HubSpot.");
        } catch (Exception e) {
            log.error("Erro inesperado ao processar autenticação no HubSpot", e);
            throw new HubspotOAuthException("Erro desconhecido ao autenticar com o HubSpot.");
        }
	}
}
