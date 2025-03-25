package com.ggfto.meetime_hubspot.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.ggfto.meetime_hubspot.exception.HubspotAuthException;
import com.ggfto.meetime_hubspot.exception.HubspotOAuthException;
import com.ggfto.meetime_hubspot.exception.RateLimitException;
import com.ggfto.meetime_hubspot.exception.TokenMissingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HubspotAuthException.class)
	public ResponseEntity<ErrorResponse> handleHubspotAuthException(HubspotAuthException e) {
		log.error("Erro na autenticação do HubSpot: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(e.getMessage()));
	}
	
	@ExceptionHandler(HubspotOAuthException.class)
	public ResponseEntity<ErrorResponse> handleHubspotOAuthException(HubspotOAuthException e) {
		log.error("Erro durante fluxo OAuth: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(e.getMessage()));
	}
	
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParsingException(HttpMessageNotReadableException e) {
        log.error("Erro ao processar o JSON recebido: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(new ErrorResponse("Erro ao processar o JSON recebido."));
    }
    
    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<ErrorResponse> handleTokenMissingException(TokenMissingException e) {
        log.error("Acesso não autorizado: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Acesso não autorizado."));
    }
    
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException e) {
        log.error("'Rate limit' excedido: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponse(e.getMessage()));
    }
    
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ErrorResponse> handleConflict(HttpClientErrorException.Conflict e) {
        log.error("Registro já existente: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Registro já existente."));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
    	log.error("Tipo de Mídia não suportado: {}", e.getMessage(), e);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(new ErrorResponse("Tipo de mídia não suportado."));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Erro inesperado: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Ocorreu um erro interno. Tente novamente mais tarde."));
    }
}
