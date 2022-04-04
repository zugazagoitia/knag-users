package com.zugazagoitia.knag.users.services.crypto;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

	private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

	public static String generateNewRefreshToken() {
		byte[] randomBytes = new byte[375];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}

	public static String generateNewSessionToken() {
		byte[] randomBytes = new byte[24];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}
}
