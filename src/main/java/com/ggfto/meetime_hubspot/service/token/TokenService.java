package com.ggfto.meetime_hubspot.service.token;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.ggfto.meetime_hubspot.exception.TokenMissingException;
import com.ggfto.meetime_hubspot.model.Token;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
public class TokenService implements ITokenService {
	
	private final AtomicReference<Token> tokenRef = new AtomicReference<>();

    @Override
    public void setToken(Token token) {
        if (token == null) {
            log.warn("Tentativa de armazenar um token nulo.");
            return;
        }
        tokenRef.set(token);
        log.info("Novo token armazenado com sucesso. Expira em: {}", token.getExpiresIn());
    }

    @Override
    public Token getToken() {
        return Optional.ofNullable(tokenRef.get())
                .orElseThrow(() -> new TokenMissingException("Tentativa de acessar um token inexistente."));
    }
}
