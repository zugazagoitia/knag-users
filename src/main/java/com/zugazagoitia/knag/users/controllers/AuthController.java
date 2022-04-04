package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.model.forms.LoginForm;
import com.zugazagoitia.knag.users.model.responses.ApiResponse;
import com.zugazagoitia.knag.users.model.responses.RefreshTokenResponse;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.repositories.RefreshTokenRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class AuthController {

	private final UserRepository userRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	@Autowired
	public AuthController(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Operation(summary = "Log in using credentials")
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

		User user = userRepository.findByEmail(loginForm.getEmail()).orElse(null);

		if (user == null) {
			throw new UnauthorizedException();
		} else {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);
			if (passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
				if (user.isEmailVerified()) {
					RefreshToken rt = new RefreshToken(user);
					refreshTokenRepository.insert(rt);
					return new RefreshTokenResponse(rt, 201, "success");
				} else {
					return new SuccessfulResponse(212, "email not verified");
				}

			} else {
				throw new UnauthorizedException();
			}


		}

	}

	@Operation(summary = "Log out using refresh token")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token removed", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Token was not deleted", content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Wrong credentials", content = @Content)
	})
	@PostMapping(path = "/v1/logout",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse logout(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String token) {

		if (refreshTokenRepository.existsById(token)) {
			refreshTokenRepository.deleteById(token);
			return new SuccessfulResponse("success");
		} else {
			throw new UnauthorizedException();
		}

	}

}
