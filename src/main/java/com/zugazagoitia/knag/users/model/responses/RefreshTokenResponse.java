package com.zugazagoitia.knag.users.model.responses;

import com.zugazagoitia.knag.users.model.RefreshToken;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenResponse extends SuccessfulResponse {

	String email;

	String refresh_token;

	String token_type;

	long creation;

	long expiration;

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

}
