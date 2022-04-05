package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.SessionToken;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.model.responses.SessionTokenResponse;
import com.zugazagoitia.knag.users.repositories.RefreshTokenRepository;
import com.zugazagoitia.knag.users.services.crypto.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SessionController {


	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtProvider jwtProvider;

	@Autowired
	public SessionController(RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProvider = jwtProvider;
	}

	@PostMapping("/v1/session")
	@ResponseBody
	public SessionTokenResponse createSession(@RequestHeader("Authorization") String authorization) {

		String token = authorization.substring(7);

		RefreshToken refreshToken = refreshTokenRepository.findById(token).orElseThrow(UnauthorizedException::new);

		SessionToken sessionToken = new SessionToken(refreshToken);

		String jwt = jwtProvider.jwtFromSessionToken(sessionToken);


		return new SessionTokenResponse(jwt);
	}

}
