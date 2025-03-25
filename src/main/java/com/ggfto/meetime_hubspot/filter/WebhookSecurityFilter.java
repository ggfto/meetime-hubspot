package com.ggfto.meetime_hubspot.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WebhookSecurityFilter extends OncePerRequestFilter {
	
	@Value("${hubspot.client-secret}")
    private String webhookSecret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String signature = request.getHeader("X-HubSpot-Signature");
        if (signature == null || !isValidSignature(request, signature)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Assinatura inválida");
            return;
        }
        filterChain.doFilter(request, response);
	}

	private boolean isValidSignature(HttpServletRequest request, String receivedSignature) {
        try {
            String body = request.getReader().lines().collect(Collectors.joining());
            SecretKeySpec key = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            String calculatedSignature = Base64.getEncoder().encodeToString(mac.doFinal(body.getBytes()));

            return receivedSignature.equals(calculatedSignature);
        } catch (Exception e) {
            return false;
        }
    }
}
