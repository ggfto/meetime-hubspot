package com.ggfto.meetime_hubspot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ggfto.meetime_hubspot.exception.TokenMissingException;
import com.ggfto.meetime_hubspot.model.Token;
import com.ggfto.meetime_hubspot.service.token.TokenService;

@SpringBootTest
public class TokenServiceTest {
	private TokenService tokenService;

	@BeforeEach
	void setUp() {
		tokenService = new TokenService();
	}

	@Test
	void deveArmazenarETrazerOTokenComSucesso() {
	    Token token = new Token("access_token", "refresh_token", 1800);
	    
	    tokenService.setToken(token);
	    Token recuperado = tokenService.getToken();

	    assertNotNull(recuperado);
	    assertEquals("access_token", recuperado.getAccessToken());
	    assertEquals(1800, recuperado.getExpiresIn());
	}

	@Test
	void deveLancarExcecaoQuandoNaoHaTokenArmazenado() {
		assertThrows(TokenMissingException.class, () -> tokenService.getToken());
	}

	@Test
	void deveManterOTokenAntigoSeOTokenNovoForNulo() {
		Token token = new Token("valid_token", "refresh_token", 1800);
		tokenService.setToken(token);

		tokenService.setToken(null); // Tentativa de setar um token nulo

		Token recuperado = tokenService.getToken();
		assertEquals("valid_token", recuperado.getAccessToken()); // Deve continuar com o token antigo
	}

	@Test
	void deveSubstituirOTokenSeNovoForValido() {
		Token primeiroToken = new Token("first_token", "refresh_token", 3600);
		tokenService.setToken(primeiroToken);

		Token segundoToken = new Token("second_token", "refresh_token", 7200);
		tokenService.setToken(segundoToken);

		Token recuperado = tokenService.getToken();
		assertEquals("second_token", recuperado.getAccessToken());
	}
}
