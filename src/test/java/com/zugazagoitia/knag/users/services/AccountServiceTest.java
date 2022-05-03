package com.zugazagoitia.knag.users.services;

import com.zugazagoitia.knag.users.model.EmailVerificationToken;
import com.zugazagoitia.knag.users.model.Role;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.repositories.EmailVerificationRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import com.zugazagoitia.knag.users.services.mail.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

class AccountServiceTest {
	@Mock
	MailService mailService;
	@Mock
	UserRepository userRepository;
	@Mock
	EmailVerificationRepository emailVerificationRepository;
	AccountService accountService;

	private final User validUser = new User("name", "surName", "valid@email.com", "$2a$16$t5jptEZ15lWDkKDJZ6zxdOC8.hA5fWJP2z9PpbI0I44lp9A3GngPy", Role.USER, false);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.accountService = new AccountService(mailService,userRepository, emailVerificationRepository);
	}

	@Test
	void testCreateUser() {

		when(userRepository.insert(any(User.class))).then(returnsFirstArg());

		User result = accountService.createUser("name", "surname", "email", "password");

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

		assertEquals("name", result.getName());
		assertEquals("surname", result.getSurname());
		assertEquals("email", result.getEmail());
		assertTrue(passwordEncoder.matches("password", result.getPassword()));
		assertEquals(Role.USER, result.getRole());
		assertFalse(result.isEmailVerified());

		verify(userRepository, times(1)).insert(any(User.class));
		verify(mailService, times(1)).sendVerificationEmail(any(EmailVerificationToken.class));
		verify(emailVerificationRepository, times(1)).insert(any(EmailVerificationToken.class));

	}

	@Test
	void testUserExistsWithEmail() {
		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		boolean result = accountService.userExistsWithEmail("email");
		assertEquals(true, result);
	}

	@Test
	void testValidateEmail() {
		when(emailVerificationRepository.findById(any())).thenReturn(Optional.of(new EmailVerificationToken(validUser)));
		when(userRepository.findById(any())).thenReturn(Optional.of(validUser));

		boolean result = accountService.validateEmail("id");

		assertEquals(true, result);
		verify(emailVerificationRepository, times(1)).delete(any(EmailVerificationToken.class));
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testValidateEmailWithInvalidToken() {
		when(emailVerificationRepository.findById(anyString())).thenReturn(Optional.empty());

		boolean result = accountService.validateEmail("id");

		assertEquals(false, result);
		verify(userRepository, times(0)).findById(anyString());
		verify(emailVerificationRepository, times(0)).delete(any(EmailVerificationToken.class));
		verify(userRepository, times(0)).save(any(User.class));
	}
}

