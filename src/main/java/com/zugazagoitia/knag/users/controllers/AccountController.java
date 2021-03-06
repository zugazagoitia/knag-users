package com.zugazagoitia.knag.users.controllers;


import com.zugazagoitia.knag.users.model.exceptions.CaptchaException;
import com.zugazagoitia.knag.users.model.forms.RegisterForm;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.services.AccountService;
import com.zugazagoitia.knag.users.services.captcha.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Controller
public class AccountController {

	final CaptchaService captchaService;

	final AccountService accountService;


	@Autowired
	public AccountController(CaptchaService captchaService, AccountService accountService) {
		this.captchaService = captchaService;
		this.accountService = accountService;

	}

	@Operation(summary = "Register an Account")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Account created", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Wrong Captcha or Body", content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email is already registered", content = @Content)
	})
	@PostMapping(path = "/v1/account/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public SuccessfulResponse register(@Valid @RequestBody RegisterForm registerForm) {

		try {
			captchaService.processResponse(registerForm.getCaptcha());
		} catch (CaptchaException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Captcha or Body");
		}

		if (accountService.userExistsWithEmail(registerForm.getEmail())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
		}

		accountService.createUser(registerForm.getName(),
				registerForm.getSurname(),
				registerForm.getEmail(),
				registerForm.getPassword());

		return new SuccessfulResponse(201, "User created, please validate email before logging in.");
	}


	@Operation(summary = "Verify Email")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email verified", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Wrong or unknown token", content = @Content)
	})
	@PostMapping(path = "/v1/account/verifyEmail",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SuccessfulResponse verifyEmail(@NotEmpty @RequestParam String id) {

		if(accountService.validateEmail(id))
			return new SuccessfulResponse(200, "Success");
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong or unknown token");
	}
}
