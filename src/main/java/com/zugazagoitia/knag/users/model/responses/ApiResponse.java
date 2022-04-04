package com.zugazagoitia.knag.users.model.responses;

public abstract class ApiResponse {

	private final int status;

	private final String response;

	public ApiResponse(int status, String response) {
		this.status = status;
		this.response = response;
	}

	public int getStatus() {
		return status;
	}

	public String getResponse() {
		return response;
	}
}
