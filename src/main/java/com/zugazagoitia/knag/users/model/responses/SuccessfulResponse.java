package com.zugazagoitia.knag.users.model.responses;

public class SuccessfulResponse extends ApiResponse {
	public SuccessfulResponse(String response) {
		this(200, response);
	}

	public SuccessfulResponse(int status, String response) {
		super(status, response);
	}
}
