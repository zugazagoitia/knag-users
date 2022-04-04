package com.zugazagoitia.knag.users.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The captcha is not valid")
public class CaptchaException extends RuntimeException {

}
