package com.zugazagoitia.knag.users.services;


import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.SessionToken;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.exceptions.UnauthorizedException;
import com.zugazagoitia.knag.users.repositories.RefreshTokenRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import com.zugazagoitia.knag.users.services.crypto.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("authService")
public class AuthService {


	private final UserRepository userRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtProvider jwtProvider;


	@Autowired
	public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProvider = jwtProvider;
	}


	/**
	 * Authenticates a user and returns a {@link RefreshToken} if the credentials are valid.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return An {@link java.util.Optional} containing a {@link RefreshToken} if the credentials are valid, an empty {@link java.util.Optional} otherwise.
	 */
	public Optional<RefreshToken> authenticate(String email, String password) {
		User user = userRepository.findByEmail(email).get();

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

		if (passwordEncoder.matches(password, user.getPassword())) {
			RefreshToken rt = new RefreshToken(user);
			refreshTokenRepository.insert(rt);
			return Optional.of(rt);
		} else {
			return Optional.empty();
		}

	}

	/**
	 * Returns a new session token if the given refresh token is valid.
	 * @param refreshToken The refresh token.
	 * @return A new session token if the given refresh token is valid.
	 */
	public String createSessionToken(String refreshToken) {
		RefreshToken token = refreshTokenRepository.findById(refreshToken).get();

		SessionToken sessionToken = new SessionToken(token);

		return jwtProvider.jwtFromSessionToken(sessionToken);
	}

	/**
	 * Deletes the refresh token from the database.
	 * @param refreshToken The refresh token.
	 * @return True if the refresh token was deleted, false otherwise.
	 */
	public boolean deleteRefreshToken(String refreshToken) {
		return refreshTokenRepository.deleteRefreshTokenById(refreshToken);
	}

	/**
	 * Checks if the given refresh token exists in the database.
	 * @param refreshToken The refresh token.
	 * @return True if the refresh token exists, false otherwise.
	 */
	public boolean refreshTokenExists(String refreshToken) {
		return refreshTokenRepository.refreshTokenExists(refreshToken);
	}

	/**
	 * Checks if the given user exists in the database.
	 * @param email The email of the user.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean userExists(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Checks if the given user has a verified email.
	 * @param email The email of the user.
	 * @return True if the user has a verified email, false otherwise.
	 */
	public boolean isEmailVerified(String email) {
		return userRepository.findByEmail(email).get().isEmailVerified();
	}

}
