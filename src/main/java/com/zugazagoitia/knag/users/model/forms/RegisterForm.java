package com.zugazagoitia.knag.users.model.forms;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class RegisterForm {

	@NotEmpty @Email
	String email;

	@NotEmpty
	@Size(min = 8, max = 256)
	String password;

	@NotEmpty
	@Size(min = 3, max = 256)
	String name;

	@NotEmpty
	@Size(min = 3, max = 256)
	String surname;

	@NotEmpty
	String captcha;

}
