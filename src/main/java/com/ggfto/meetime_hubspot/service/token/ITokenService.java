package com.ggfto.meetime_hubspot.service.token;

import com.ggfto.meetime_hubspot.model.Token;

public interface ITokenService {
	void setToken(Token token);
	Token getToken();
}
