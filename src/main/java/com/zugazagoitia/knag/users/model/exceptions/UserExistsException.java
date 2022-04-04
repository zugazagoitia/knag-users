package com.zugazagoitia.knag.users.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email already in use")
public class UserExistsException extends RuntimeException {
}
