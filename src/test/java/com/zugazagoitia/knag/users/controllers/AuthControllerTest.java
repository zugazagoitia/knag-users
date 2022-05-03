package com.zugazagoitia.knag.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zugazagoitia.knag.users.model.RefreshToken;
import com.zugazagoitia.knag.users.model.Role;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.forms.LoginForm;
import com.zugazagoitia.knag.users.services.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	private final User validUser = new User("name", "surName", "valid@email.com", "password", Role.USER, false);
	private final LoginForm validLoginForm = new LoginForm("notverified@email.com", "password", null, null);

	private final LoginForm validLoginFormAndValidEmail = new LoginForm("valid@email.com", "password", null, null);
	private final RefreshToken validToken = new RefreshToken(validUser, Instant.EPOCH, Instant.MAX, null, null, "validToken");

	@BeforeEach
	private void setUp() {
		when(authService.authenticate(anyString(), anyString())).thenReturn(Optional.empty());
		when(authService.authenticate(validLoginForm.getEmail(), validLoginForm.getPassword())).thenReturn(Optional.of(validToken));
		when(authService.authenticate(validLoginFormAndValidEmail.getEmail(), validLoginFormAndValidEmail.getPassword())).thenReturn(Optional.of(validToken));

		when(authService.refreshTokenExists(anyString())).thenReturn(false);
		when(authService.refreshTokenExists(validToken.getToken())).thenReturn(true);

		when(authService.deleteRefreshToken(anyString())).thenReturn(false);
		when(authService.deleteRefreshToken(validToken.getToken())).thenReturn(true);

		when(authService.createSessionToken(anyString())).thenReturn(null);
		when(authService.createSessionToken(validToken.getToken())).thenReturn("sessionToken");

		when(authService.isEmailVerified(anyString())).thenReturn(false);
		when(authService.isEmailVerified(validLoginFormAndValidEmail.getEmail())).thenReturn(true);

		when(authService.userExists(anyString())).thenReturn(false);
		when(authService.userExists(validUser.getEmail())).thenReturn(true);
		when(authService.userExists(validLoginForm.getEmail())).thenReturn(true);
	}

	@Test
	void validLogin() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validLoginFormAndValidEmail)))
				.andExpect(status().isCreated());

	}

	@Test
	void validLoginInvalidEmail() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validLoginForm)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("email not verified")));

	}

	@Test
	void malformedLogin() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginForm("", "", null, null))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void invalidEmailLogin() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginForm("notvalid", "password", null, null))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shortPasswordLogin() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginForm("valid@email.com", "pass", null, null))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void wrongCreditentialsLogin() throws Exception {
		this.mockMvc.perform(post("/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new LoginForm("valid@email.com", "wrongpassword", null, null))))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void correctLogout() throws Exception {
		this.mockMvc.perform(post("/v1/auth/logout")
						.header("Authorization", "Bearer " + validToken.getToken()))
				.andExpect(status().isOk());
	}

	@Test
	void wrongLogout() throws Exception {
		this.mockMvc.perform(post("/v1/auth/logout")
						.header("Authorization", "Bearer " + "wrongToken"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void malformedLogout() throws Exception {
		this.mockMvc.perform(post("/v1/auth/logout")
						.header("Authorization", "Bearer "))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void missingHeaderLogout() throws Exception {
		this.mockMvc.perform(post("/v1/auth/logout"))
				.andExpect(status().isBadRequest());
	}


	@Test
	void validRefresh() throws Exception {
		this.mockMvc.perform(post("/v1/auth/refresh")
						.header("Authorization", "Bearer " + validToken.getToken()))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("sessionToken")));
	}

	@Test
	void wrongRefresh() throws Exception {
		this.mockMvc.perform(post("/v1/auth/refresh")
						.header("Authorization", "Bearer " + "wrongToken"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void malformedRefresh() throws Exception {
		this.mockMvc.perform(post("/v1/auth/refresh")
						.header("Authorization", "Bearer "))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void missingHeaderRefresh() throws Exception {
		this.mockMvc.perform(post("/v1/auth/refresh"))
				.andExpect(status().isBadRequest());
	}

}