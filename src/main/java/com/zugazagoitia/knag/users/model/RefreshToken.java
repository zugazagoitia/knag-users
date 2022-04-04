package com.zugazagoitia.knag.users.model;

import com.zugazagoitia.knag.users.services.crypto.TokenGenerator;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Value
public class RefreshToken {

	User user;

	Instant creation;

	Instant expiration;
	@MongoId
	String token;

	@NonFinal
	String deviceName;
	@NonFinal
	String deviceType;

	public RefreshToken(User user) {
		this.user = user;
		this.creation = Instant.now();
		this.expiration = creation.plus(200, ChronoUnit.DAYS);
		this.token = TokenGenerator.generateNewRefreshToken();
	}

	public RefreshToken(User user, String deviceName, String deviceType) {
		this(user);
		this.deviceName = deviceName;
		this.deviceType = deviceType;
	}

	@PersistenceConstructor
	public RefreshToken(User user, Instant creation, Instant expiration, String deviceName, String deviceType, String token) {
		this.user = user;
		this.creation = creation;
		this.expiration = expiration;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.token = token;
	}

}
