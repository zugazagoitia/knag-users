package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.model.forms.LoginForm;
import com.zugazagoitia.knag.users.model.responses.ApiResponse;
import com.zugazagoitia.knag.users.model.responses.RefreshTokenResponse;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthController {

	final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "Log in using credentials", description = "Returns a refresh token if credentials are correct", tags = {"auth"})
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Token created", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = RefreshTokenResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "212", description = "The Email has not been verified", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Wrong credentials", content = @Content)
	})
	@PostMapping(path = "/v1/auth",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse login(@Valid @RequestBody LoginForm loginForm) {

		//TODO save device type/name

		if (!authService.userExists(loginForm.getEmail())) {
			throw new UnauthorizedException();
		} else {
			Optional<RefreshToken> token = authService.authenticate(loginForm.getEmail(), loginForm.getPassword());
			if (token.isPresent()) {
				return new RefreshTokenResponse(token.get(), 201, "success");
			} else if (!authService.isEmailVerified(loginForm.getEmail())) {
				return new SuccessfulResponse(212, "email not verified");
			} else {
				throw new UnauthorizedException();
			}
		}

	}

	@Operation(summary = "Log out using refresh token", description = "Deletes the refresh token if it exists", tags = {"auth"})
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token removed", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Token was not deleted", content = @Content)
	})
	@PostMapping(path = "/v1/logout",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse logout(@RequestHeader("Authorization") String authorizationHeader) {

		String token = authorizationHeader.substring(7);

		if (authService.deleteRefreshToken(token))
			return new SuccessfulResponse("success");
		else
			throw new UnauthorizedException();

	}

}
