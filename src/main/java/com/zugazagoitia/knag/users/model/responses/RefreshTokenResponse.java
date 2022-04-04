package com.zugazagoitia.knag.users.model.responses;

import com.zugazagoitia.knag.users.model.RefreshToken;

public class RefreshTokenResponse extends ApiResponse {

	private final String email;

	private final String refresh_token;

	private final String token_type;

	private final long creation;

	private final long expiration;

	private RefreshTokenResponse(RefreshToken refreshToken, int status, String message, String token_type) {
		super(status, message);
		this.email = refreshToken.getUser().getEmail();
		this.refresh_token = refreshToken.getToken();
		this.expiration = refreshToken.getExpiration().getEpochSecond();
		this.creation = refreshToken.getCreation().getEpochSecond();
		this.token_type = token_type;
	}

	public RefreshTokenResponse(RefreshToken refreshToken, int status, String message) {
		this(refreshToken, status, message, "Bearer");
	}

	public String getEmail() {
		return email;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public long getExpiration() {
		return expiration;
	}

	public long getCreation() {
		return creation;
	}

	public String getToken_type() {
		return token_type;
	}
}
