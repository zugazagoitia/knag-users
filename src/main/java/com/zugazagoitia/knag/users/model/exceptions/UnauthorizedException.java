package com.zugazagoitia.knag.users.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "wrong credentials")
public class UnauthorizedException extends RuntimeException {

}
