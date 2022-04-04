package com.zugazagoitia.knag.users.model;

import com.zugazagoitia.knag.users.services.crypto.TokenGenerator;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Value
public class SessionToken {

	User user;

	RefreshToken refreshTokenUsed;

	Instant creation;

	Instant expiration;

	@MongoId
	String token;

	public SessionToken(RefreshToken refreshToken) {
		this.user = refreshToken.getUser();
		this.refreshTokenUsed = refreshToken;
		this.creation = Instant.now();
		this.expiration = creation.plus(30, ChronoUnit.MINUTES);
		this.token = TokenGenerator.generateNewSessionToken();
	}

}
