package com.zugazagoitia.knag.users;

import com.zugazagoitia.knag.users.services.captcha.CaptchaSettings;
import com.zugazagoitia.knag.users.services.crypto.KeySettings;
import com.zugazagoitia.knag.users.services.mail.MailSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties({KeySettings.class, MailSettings.class, CaptchaSettings.class})
@SpringBootApplication //(exclude = { SecurityAutoConfiguration.class })
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
