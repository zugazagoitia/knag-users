package com.zugazagoitia.knag.users.model.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterForm {

	@NotEmpty
	@Email
	private final String email;
	@Size(min = 8, max = 256)
	private final String password;
	@Size(min = 3, max = 256)
	private final String name;
	@Size(min = 3, max = 256)
	private final String surname;
	private final String captcha;

	public RegisterForm(String email, String password, String name, String surname, String captcha) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.captcha = captcha;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getCaptcha() {
		return captcha;
	}
}
