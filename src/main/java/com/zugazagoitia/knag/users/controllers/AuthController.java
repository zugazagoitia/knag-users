package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.model.forms.LoginForm;
import com.zugazagoitia.knag.users.model.responses.ApiResponse;
import com.zugazagoitia.knag.users.model.responses.RefreshTokenResponse;
import com.zugazagoitia.knag.users.model.responses.SessionTokenResponse;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Controller
public class AuthController {

	final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "Log in using credentials", description = "Returns a refresh token if credentials are correct", tags = {"Authentication"})
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Token created", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = RefreshTokenResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "212", description = "The Email has not been verified", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "400",
					description = "Malformed Request",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "401",
					description = "Wrong credentials",
					content = @Content)
	})
	@PostMapping(path = "/v1/auth/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<? extends ApiResponse> login(@Valid @RequestBody LoginForm loginForm) {

		//TODO save device type/name

		if (!authService.userExists(loginForm.getEmail())) {
			throw new UnauthorizedException();
		} else {
			Optional<RefreshToken> token = authService.authenticate(loginForm.getEmail(), loginForm.getPassword());
			if (token.isPresent()) {
				if (authService.isEmailVerified(loginForm.getEmail())) {
					return new ResponseEntity<>(new RefreshTokenResponse(token.get(), 201, "success"), HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(new SuccessfulResponse(212, "email not verified"), HttpStatus.OK);
				}
			} else {
				throw new UnauthorizedException();
			}
		}

	}

	@Operation(summary = "Log out using refresh token", description = "Deletes the refresh token if it exists", tags = {"Authentication"})
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token removed", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "400",
					description = "Malformed Request",
					content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "401",
					description = "Token was not deleted",
					content = @Content)
	})
	@PostMapping(path = "/v1/auth/logout",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<? extends ApiResponse> logout(@NotEmpty @RequestHeader("Authorization") String authorizationHeader) {

		String token = authorizationHeader.substring(7);

		if (authService.deleteRefreshToken(token))
			return new ResponseEntity<>(new SuccessfulResponse("success"), HttpStatus.OK);
		else
			throw new UnauthorizedException();

	}

	@Operation(summary = "Get a session token",
			description = "Retrieves a session token for a given refresh token",
			tags = {"Authentication"}
	)
	@ApiResponses(
			value = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(
							responseCode = "200",
							description = "Successful response",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = SessionTokenResponse.class)
							)
					),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(
							responseCode = "400",
							description = "Malformed Request",
							content = @Content
					),

					@io.swagger.v3.oas.annotations.responses.ApiResponse(
							responseCode = "401",
							description = "Unauthorized",
							content = @Content
					)
			}
	)
	@PostMapping(value = "/v1/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SessionTokenResponse createSession(@NotEmpty @RequestHeader("Authorization") String authorization) {

		String refreshToken = authorization.substring(7);

		if (authService.refreshTokenExists(refreshToken))
			return new SessionTokenResponse(authService.createSessionToken(refreshToken));
		else
			throw new UnauthorizedException();


	}

}
