package com.zugazagoitia.knag.users.services.crypto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

	private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

	public static String generateNewToken(TokenType type) {
		byte[] randomBytes = new byte[type.getValue()];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}



	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public enum TokenType{
		REFRESH(375),EMAIL(200),SESSION(24);

		@Getter
		private final int value;
	}
}
