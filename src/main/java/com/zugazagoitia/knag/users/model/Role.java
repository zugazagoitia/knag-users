package com.zugazagoitia.knag.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {
	ADMIN("ADMIN"),
	USER("USER"),
	SUBSCRIBER("SUBSCRIBER"),
	GUEST("SUBSCRIBER");

	@Getter private String value;
}
