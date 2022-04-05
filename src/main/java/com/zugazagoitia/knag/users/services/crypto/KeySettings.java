package com.zugazagoitia.knag.users.services.crypto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "knag.key")
@Data
public class KeySettings {
	private String pub;
	private String priv;
}
