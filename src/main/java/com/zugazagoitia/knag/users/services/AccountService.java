package com.zugazagoitia.knag.users.services;

import com.zugazagoitia.knag.users.model.EmailVerificationToken;
import com.zugazagoitia.knag.users.model.Role;
import com.zugazagoitia.knag.users.model.User;
import com.zugazagoitia.knag.users.model.responses.SuccessfulResponse;
import com.zugazagoitia.knag.users.repositories.EmailVerificationRepository;
import com.zugazagoitia.knag.users.repositories.UserRepository;
import com.zugazagoitia.knag.users.services.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Service("accountService")
public class AccountService {

	final MailService mailService;

	final UserRepository userRepository;

	final EmailVerificationRepository emailVerificationRepository;

	@Autowired
	public AccountService(MailService mailService, UserRepository userRepository, EmailVerificationRepository emailVerificationRepository) {
		this.mailService = mailService;
		this.userRepository = userRepository;
		this.emailVerificationRepository = emailVerificationRepository;
	}

	/**
	 * Creates a new user and sends an email to the user with a token to verify the email
	 * @param name the name of the user
	 * @param surname the surname of the user
	 * @param email the email of the user
	 * @param password the raw password of the user
	 * @return the user created
	 */
	public User createUser(String name, String surname, String email, String password){

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

		User newUser = new User(name,
				surname,
				email,
				passwordEncoder.encode(password),
				Role.USER,
				false);


		User createdUser = userRepository.insert(newUser);

		EmailVerificationToken emailToken = new EmailVerificationToken(createdUser);

		mailService.sendVerificationEmail(emailToken);

		emailVerificationRepository.insert(emailToken);

		return createdUser;

	}


	/**
	 * Checks if the user is already registered
	 * @param email the email to check
	 * @return true if the user is already registered, false otherwise
	 */
	public boolean userExistsWithEmail(String email){
		 return userRepository.existsByEmail(email);
	}

	/**
	 * Verifies the email token and activates the user
	 * @param id the token to verify
	 * @return true if the token is valid and the user is activated, false otherwise
	 */
	public boolean validateEmail(String id){

		Optional<EmailVerificationToken> token = emailVerificationRepository.findById(id);

		if(token.isPresent()){

			User user = userRepository.findById(token.get().getUser().getId()).get();
			user.setEmailVerified(true);

			userRepository.save(user);
			emailVerificationRepository.delete(token.get());

			return true;

		}else return false;

	}
}
