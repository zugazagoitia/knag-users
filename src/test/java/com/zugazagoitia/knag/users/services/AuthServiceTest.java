package com.zugazagoitia.knag.users.services;

import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.Role;
import com.zugazagoitia.knag.users.model.SessionToken;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.forms.LoginForm;
import com.zugazagoitia.knag.users.repositories.RefreshTokenRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import com.zugazagoitia.knag.users.services.crypto.JwtProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private JwtProvider jwtProvider;

	private AuthService authService;


	private final User validUser = new User("name", "surName", "valid@email.com", "$2a$16$t5jptEZ15lWDkKDJZ6zxdOC8.hA5fWJP2z9PpbI0I44lp9A3GngPy", Role.USER, false);

	private final LoginForm validLoginForm = new LoginForm("valid@email.com", "password", null, null);

	private final RefreshToken validToken = new RefreshToken(validUser, Instant.EPOCH, Instant.MAX, null, null, "validToken");

	@BeforeEach
	void setUp() {
		authService = new AuthService(userRepository, refreshTokenRepository, jwtProvider);
	}

	@AfterEach
	void tearDown() {
		clearInvocations(userRepository, refreshTokenRepository);
	}

	@Test
	void testSuccessfulAuthenticate() {

		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(Optional.of(validUser));


		var result = authService.authenticate(validLoginForm.getEmail(), validLoginForm.getPassword());

		assertTrue(result.isPresent());
		assertEquals(validUser, result.get().getUser());

	}

	@Test
	void testInvalidPasswordAuthenticate() {

		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(validLoginForm.getEmail())).thenReturn(Optional.of(validUser));


		var result = authService.authenticate(validLoginForm.getEmail(), "invalidPassword");

		assertFalse(result.isPresent());

	}

	@Test
	void testInvalidEmailAuthenticate() {

		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () ->
				authService.authenticate("wrong@email.com", validLoginForm.getPassword())
		);

	}


	@Test
	void testCreateSessionToken() {

		when(refreshTokenRepository.findById("refreshToken")).thenReturn(Optional.of(validToken));
		when(jwtProvider.jwtFromSessionToken(any())).thenReturn("jwtFromSessionTokenResponse");

		String result = authService.createSessionToken("refreshToken");
		Assertions.assertEquals("jwtFromSessionTokenResponse", result);

	}

	@Test
	void testDeleteRefreshToken() {
		when(refreshTokenRepository.deleteRefreshTokenByToken(anyString())).thenReturn(true);

		boolean result = authService.deleteRefreshToken("refreshToken");
		Assertions.assertEquals(true, result);
	}

	@Test
	void testRefreshTokenExists() {
		when(refreshTokenRepository.existsByToken(anyString())).thenReturn(true);

		boolean result = authService.refreshTokenExists("refreshToken");
		Assertions.assertEquals(true, result);
	}

	@Test
	void testUserExists() {
		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		boolean result = authService.userExists("email");
		Assertions.assertEquals(true, result);
	}

	@Test
	void testIsEmailVerified() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		boolean result = authService.isEmailVerified("email");
		Assertions.assertEquals(true, result);
	}
}