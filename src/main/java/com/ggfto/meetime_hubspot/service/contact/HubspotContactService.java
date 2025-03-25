package com.ggfto.meetime_hubspot.service.contact;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ggfto.meetime_hubspot.exception.RateLimitException;
import com.ggfto.meetime_hubspot.model.dto.HubspotContactDTO;
import com.ggfto.meetime_hubspot.response.HubspotContactListResponse;
import com.ggfto.meetime_hubspot.response.HubspotContactResponse;
import com.ggfto.meetime_hubspot.service.token.ITokenService;
import com.google.common.util.concurrent.RateLimiter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubspotContactService implements IHubspotContactService {

	@Value("${hubspot.api-uri}")
	private String apiUrl;

	@Value("${hubspot.contact-uri}")
	private String contactApi;

	@Value("${hubspot.rate-limit.requests:110}")
	private double maxRequests;

	@Value("${hubspot.rate-limit.interval:10}")
	private double intervalInSeconds;

	private final RestTemplate restTemplate;
	private final ITokenService tokenService;

	private RateLimiter rateLimiter;

	@PostConstruct
	public void initRateLimiter() {
		rateLimiter = RateLimiter.create(maxRequests / intervalInSeconds);
	}

	@Override
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public HubspotContactResponse createContact(HubspotContactDTO contactDTO) {
        verificarRateLimit();

        HttpEntity<HubspotContactDTO> request = new HttpEntity<>(contactDTO, getAuthHeaders());
        return sendRequestWithRetry(apiUrl + contactApi, HttpMethod.POST, request, HubspotContactResponse.class);
    }

    @Override
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public HubspotContactListResponse getContacts() {
        verificarRateLimit();

        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders());
        return sendRequestWithRetry(apiUrl + contactApi, HttpMethod.GET, request, HubspotContactListResponse.class);
    }

	private <T> T sendRequestWithRetry(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(url, method, request, responseType);
			return response.getBody();
		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
				handleRateLimit(e);
			}
			throw e;
		}
	}

	private void handleRateLimit(HttpStatusCodeException e) {
		String retryAfter = e.getResponseHeaders().getFirst("Retry-After");
		if (retryAfter != null) {
			long waitTime = Long.parseLong(retryAfter) * 1000;
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + tokenService.getToken().getAccessToken());
		return headers;
	}

	private void verificarRateLimit() {
		if (!rateLimiter.tryAcquire()) {
			throw new RateLimitException("'Rate limit' excedido, tente novamente mais tarde.");
		}
	}
}
