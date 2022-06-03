package com.zugazagoitia.knag.users.services.mail;

import com.zugazagoitia.knag.users.model.EmailVerificationToken;
import lombok.extern.java.Log;
import net.sargue.mailgun.Configuration;

import net.sargue.mailgun.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.logging.Level;

@Log
@Service("mailService")
public class MailService {

	private final Configuration configuration;

	public MailService(@Value("#{mailSettings.apiKey}") String apiKey,
	                   @Value("#{mailSettings.apiBaseUrl}") String apiBaseUrl,
	                   @Value("#{mailSettings.domainName}") String domainName,
	                   @Value("#{mailSettings.sender}") String sender) {

		log.log(Level.CONFIG, "Mailgun api key: " + apiKey);
		log.log(Level.CONFIG, "Mailgun api base url: " + apiBaseUrl);
		log.log(Level.CONFIG, "Mailgun domain name: " + domainName);
		log.log(Level.CONFIG, "Mailgun sender: " + sender);


		configuration = new Configuration()
				.apiKey(apiKey)
				.from("Knag",sender)
				.apiUrl(apiBaseUrl)
				.domain(domainName);
	}



	public void sendVerificationEmail(EmailVerificationToken token){

		String htmlText = ""; //TODO Replace name and token on link and design template
		String plainText = MessageFormat.format("Your token for verification is: \"{0}\" \n", token.getToken());

		Mail.using(configuration)
				.to(token.getUser().getName(), token.getUser().getEmail())
				.subject("Validate your e-mail address")
				.html(htmlText)
				.text(plainText)
				.build()
				.send();

	}

}
