package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.SessionToken;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.model.responses.RefreshTokenResponse;
import com.zugazagoitia.knag.users.model.responses.SessionTokenResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SessionController {

	private AuthService authService;

	@Autowired
	public SessionController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "Get a session token",
			description = "Get a session token",
			tags = {"session"}
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
							responseCode = "401",
							description = "Unauthorized",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = UnauthorizedException.class)
							)
					)
			}
	)
	@PostMapping(value = "/v1/session", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SessionTokenResponse createSession(@RequestHeader("Authorization") String authorization) {

		String refreshToken = authorization.substring(7);

		if (authService.refreshTokenExists(refreshToken))
			return new SessionTokenResponse(authService.createSessionToken(refreshToken));
		else
			throw new UnauthorizedException();


	}

}
