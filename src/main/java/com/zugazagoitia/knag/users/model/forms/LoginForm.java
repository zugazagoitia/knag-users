package com.zugazagoitia.knag.users.model.forms;


import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class LoginForm {

	@Email
	@NotEmpty
	String email;

	@Size(min = 8, max = 256)
	@NotEmpty
	String password;

	@NonFinal
	String deviceName;
	@NonFinal
	String deviceType;

}
