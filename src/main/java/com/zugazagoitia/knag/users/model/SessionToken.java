package com.zugazagoitia.knag.users.model;

import com.zugazagoitia.knag.users.utils.crypto.TokenGenerator;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SessionToken {

	private final User user;

	private final RefreshToken refreshTokenUsed;

	private final Instant creation;

	private final Instant expiration;

	@MongoId
	private final String token;

	public SessionToken(RefreshToken refreshToken) {
		this.user = refreshToken.getUser();
		this.refreshTokenUsed = refreshToken;
		this.creation = Instant.now();
		this.expiration = creation.plus(30, ChronoUnit.MINUTES);
		this.token = TokenGenerator.generateNewSessionToken();
	}


	public User getUser() {
		return user;
	}

	public RefreshToken getRefreshTokenUsed() {
		return refreshTokenUsed;
	}

	public Instant getCreation() {
		return creation;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public String getToken() {
		return token;
	}
}
