package com.zugazagoitia.knag.users.model.forms;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class RegisterForm {

	@NotEmpty @Email
	String email;

	@Size(min = 8, max = 256)
	String password;

	@Size(min = 3, max = 256)
	String name;

	@Size(min = 3, max = 256)
	String surname;

	@NotEmpty
	String captcha;

}
