package com.zugazagoitia.knag.users.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zugazagoitia.knag.users.model.Role;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.exceptions.CaptchaException;
import com.zugazagoitia.knag.users.model.forms.RegisterForm;
import com.zugazagoitia.knag.users.services.AccountService;
import com.zugazagoitia.knag.users.services.captcha.CaptchaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private CaptchaService captchaService;
	@MockBean
	private AccountService accountService;

	private final String validCaptcha =
			"03AGdBq25cz0XaC7i9oEH6yftVF0PiJRZYPaQAeOcPGQkNjPcIgNlmPhwYnw86fktanUtJ7wHa1tOknHtbEdIp8pwXv0lPLLi1CJY3wCooBJCUw4TFf9_cmOW4grRkNfNyfp_GXwz2KjWkLVaC8XRQu3ep0ebz58G0gOaC9ahoCvXpBz3uREbZlhwiFPHRyc06H3Zsp1ZN2J8ck7zTfs51Co2sBTVu8ksM8AvV1zTnADloSsMGzlNb7oQjWdaDMrOdYNCs7mHngJIVcupaTtTo53ZqlWqAu8ndxg9H5eyp-6iL7GSgKGNfAgR4yxYRlfEaY0tftBW6wFELFEJfqjcU0xjiqZX1xGYfvUg_LXpPn47HqPYpM1Vq5W4S-GX1XDIkLiaN3BiT9nMko_ZxHiN3RXBtr4lMy_xVBHrmgka172Qt0Spct2pVMgZiE1wym9z6zJ3jUGyVSevlyxh49BcXofqwh19zvHMPkBh4tVrUZMTHyrhjbGZE9iQ0zI8PpjuzkRuY2BZ-lOVyWc_K08vmdlYTwHE7SZfUV09GpLsvd9ed-4x-GwurhOeKw9ffBqn4zZWkSZT-1zpWc2e7RfUvW4qJfjOSDfHX1aAk9Oa4AOASZuxPtSlZGbMDjntPdZkTwaJEdlPQpC7XfOtyoMifHHHSwVN6TmMSkSKiVI1MVEqRTjiwstxA5MbzNes8iD45uXUbav9mZfqKnZtuJIgl35j4LJsauAvXp_5Qqz_QNaXp5rZwLAOtT7mafnuMbx50_vG0Qq1vjMnVB-F-zUv1p16ut_MagyMmkxdpVKdztUSk-VtejZNBa8Om9zyShmFh-NQ3STXHreWb1k3T2fT6WHTtRHE-oNcTGwilmjlq2rhE8g1cAk8R4UNEAO6o-9maGmW-GdmrpG5hnDlko17t_F9wczlo-Wwi6gQrVadzkwJD5LM5Xn1ZdPS_UH8suOJ8QWhwYxVRMyMMgp3N87qnw-4aAwjN8ztPyLEnutv-uixh0Y5500Bh6LGKWy1fyDkiLzc1xC8e_CxQzzjo9AqrXNDU1nByQRWUNbLGvqd0X98pt_dsiX1e8e1bnECMnEZhtsLoOVBJkOMwPmLHej4PuFq8SZ4AZmx70CIuo2dbD3AVn6fHgmtVj-XmvJblt1VSNTtNoYUj6cqzipX00CkbC0Ts5vx5LhK0DG2U7-dh3jnA_nNwyC3Vx5CQ_2gA2MLgQujFyIcXUImK";

	private final String validToken =
			"cKsgIj1vxX0hEO-yNXjFz4L5fMmBw80FbKhFDANtcF3Y7n8Oowzuw_6NG2A0k2OyDqkb9-bmRTC5sdnERf7nnAPqT5uFdrZL5ZiNo0YSEWL2ffxnhZv49tNesWekryi4lNTVTOABQlJnT1m3fLlut7ND01rRLlB-byfr0Vk9XyhDQjrTahblCT1o6GbegcQ9tl5Z463zpDBchE4nV3NPZs8iaIfBXdhXbRLNQMe41B2Q3jgXr7hkze4o3cLhXR_40iS9PyhkUpM=";

	private final RegisterForm validRegisterForm = new RegisterForm("sample@email.com", "password", "name", "surname", validCaptcha);


	@BeforeEach
	private void setupMocks() {

		doThrow(CaptchaException.class).when(captchaService).processResponse(anyString());
		doNothing().when(captchaService).processResponse(validCaptcha);

		when(accountService.createUser(validRegisterForm.getName(),
				validRegisterForm.getSurname(),
				validRegisterForm.getEmail(),
				validRegisterForm.getPassword()))
				.thenReturn(new User(validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getEmail(),
						validRegisterForm.getPassword(),
						Role.USER,
						false));

		when(accountService.userExistsWithEmail(anyString())).thenReturn(false);
		when(accountService.userExistsWithEmail("duplicate@email.com")).thenReturn(true);

		when(accountService.validateEmail(anyString())).thenReturn(false);
		when(accountService.validateEmail(validToken)).thenReturn(true);
	}

	@Test
	void validRegister() throws Exception {
		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validRegisterForm)))
				.andExpect(status().isCreated());
	}

	@Test
	void invalidCaptchaRegister() throws Exception {

		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						"wrongcaptcha");

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void invalidEmailRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm("notanemail",
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void emptyEmailRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm("",
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void emptyPasswordRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						"",
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shortPasswordRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						"short",
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void emptyNameRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						validRegisterForm.getPassword(),
						"",
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void emptySurnameRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						"",
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void emptyCaptchaRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm(validRegisterForm.getEmail(),
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						"");

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void duplicateEmailRegister() throws Exception {
		RegisterForm wrongForm =
				new RegisterForm("duplicate@email.com",
						validRegisterForm.getPassword(),
						validRegisterForm.getName(),
						validRegisterForm.getSurname(),
						validRegisterForm.getCaptcha());

		this.mockMvc.perform(post("/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(wrongForm)))
				.andExpect(status().isConflict());
	}



	@Test
	void verifyEmailCorrectToken() throws Exception {

		when(accountService.validateEmail(validToken)).thenReturn(true);

		this.mockMvc.perform(post("/v1/verifyEmail?id=" + validToken))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":200")));
	}

	@Test
	void verifyEmailWrongToken() throws Exception {

		when(accountService.validateEmail(validToken)).thenReturn(true);

		this.mockMvc.perform(post("/v1/verifyEmail?id=" + new StringBuilder(validToken).reverse()))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void verifyEmailEmptyToken() throws Exception {

		when(accountService.validateEmail(validToken)).thenReturn(true);

		this.mockMvc.perform(post("/v1/verifyEmail?id="))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void verifyEmailEmptyToken2() throws Exception {

		when(accountService.validateEmail(validToken)).thenReturn(true);

		this.mockMvc.perform(post("/v1/verifyEmail"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void verifyEmailMalformedToken() throws Exception {

		when(accountService.validateEmail(validToken)).thenReturn(true);

		this.mockMvc.perform(post("/v1/verifyEmail?id=" + validToken.substring(0, validToken.length() / 2)))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
}