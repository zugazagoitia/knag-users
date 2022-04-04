package com.zugazagoitia.knag.users.model.forms;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginForm {

	@Email
	@NotEmpty
	private final String email;
	@Size(min = 8, max = 256)
	@NotEmpty
	private final String password;

	private String deviceName;
	private String deviceType;

	public LoginForm(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
