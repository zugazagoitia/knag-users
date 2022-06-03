package com.zugazagoitia.knag.users.services.captcha;

import com.zugazagoitia.knag.users.model.exceptions.CaptchaException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Log
@Service("captchaService")
public class CaptchaService {

	private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	private final CaptchaSettings captchaSettings;
	private final RestOperations restTemplate;

	@Autowired
	public CaptchaService(CaptchaSettings captchaSettings, RestOperations restTemplate) {
		log.log(Level.CONFIG, "Captcha secret: " + captchaSettings.getSecret());
		log.log(Level.CONFIG, "Captcha site: " + captchaSettings.getSite());

		this.captchaSettings = captchaSettings;
		this.restTemplate = restTemplate;
	}

	public void processResponse(String response) {
		if (!responseSanityCheck(response)) {
			throw new CaptchaException();
		}

		URI verifyUri = URI.create(String.format(
				"https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
				captchaSettings.getSecret(), response));

		GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

		if (!googleResponse.isSuccess()) {
			throw new CaptchaException();
		}
	}

	private boolean responseSanityCheck(String response) {
		return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
	}


}