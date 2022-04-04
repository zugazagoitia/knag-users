package com.zugazagoitia.knag.users.model.responses;

public class SessionTokenResponse extends SuccessfulResponse {

	private final String token;

	private final String token_type;

	private SessionTokenResponse(String token, String token_type) {
		super("Success");
		this.token = token;
		this.token_type = token_type;
	}

	public SessionTokenResponse(String token) {
		this(token, "Bearer");
	}

	public String getToken() {
		return token;
	}

	public String getToken_type() {
		return token_type;
	}
}
