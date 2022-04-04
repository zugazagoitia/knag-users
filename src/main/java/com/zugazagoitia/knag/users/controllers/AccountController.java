package com.zugazagoitia.knag.users.controllers;

import com.zugazagoitia.knag.users.model.EmailVerificationToken;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.exceptions.CaptchaException;
import com.zugazagoitia.knag.users.model.forms.RegisterForm;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.repositories.EmailVerificationRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import com.zugazagoitia.knag.users.services.captcha.CaptchaService;
import com.zugazagoitia.knag.users.services.mail.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Controller
public class AccountController {

	final CaptchaService captchaService;

	final MailService mailService;

	final UserRepository userRepository;

	final EmailVerificationRepository emailVerificationRepository;



	@Autowired
	public AccountController(CaptchaService captchaService, MailService mailService, UserRepository userRepository, EmailVerificationRepository emailVerificationRepository) {
		this.captchaService = captchaService;
		this.mailService = mailService;
		this.userRepository = userRepository;
		this.emailVerificationRepository = emailVerificationRepository;
	}

	@Operation(summary = "Register an Account")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Account created", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Wrong Captcha or Body", content = @Content),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email is already registered", content = @Content)
	})
	@PostMapping(path = "/v1/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SuccessfulResponse register(@Valid @RequestBody RegisterForm registerForm) {

		try {
			captchaService.processResponse(registerForm.getCaptcha());
		} catch (CaptchaException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Captcha");
		}

		if (userRepository.findByEmail(registerForm.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
		}



		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

		User newUser = new User(registerForm.getName(),
				registerForm.getSurname(),
				registerForm.getEmail(),
				passwordEncoder.encode(registerForm.getPassword()));


		User createdUser = userRepository.insert(newUser);

		EmailVerificationToken emailToken = new EmailVerificationToken(createdUser);

		mailService.sendVerificationEmail(emailToken);

		emailVerificationRepository.insert(emailToken);

		return new SuccessfulResponse(201, "User created, please validate email before logging in.");
	}


	@Operation(summary = "Validate Email")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email Validated", content = {
					@Content(mediaType = "application/json",
							schema = @Schema(implementation = SuccessfulResponse.class))
			}),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Wrong ", content = @Content)
	})
	@PostMapping(path = "/v1/verifyEmail",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SuccessfulResponse validateEmail(@NotEmpty @RequestParam String id) {

		Optional<EmailVerificationToken> token = emailVerificationRepository.findById(id);

		if(token.isPresent()){

			User user = userRepository.findById(token.get().getUser().getId()).get();
			user.setEmailVerified(true);

			userRepository.save(user);
			emailVerificationRepository.delete(token.get());

			return new SuccessfulResponse(200, "Success");

		}else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong or unknown token");


	}
}
