package com.zugazagoitia.knag.users.model.responses;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SessionTokenResponse extends SuccessfulResponse {

	String token;

	String token_type;

	private SessionTokenResponse(String token, String token_type) {
		super("Success");
		this.token = token;
		this.token_type = token_type;
	}

	public SessionTokenResponse(String token) {
		this(token, "Bearer");
	}

}
