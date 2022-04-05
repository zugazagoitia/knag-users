package com.zugazagoitia.knag.users.services.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "mailgun")
@Data
public class MailSettings {

	String apiBaseUrl;

	String domainName;

	String sender;

	String apiKey;

}
