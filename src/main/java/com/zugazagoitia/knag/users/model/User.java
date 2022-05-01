package com.zugazagoitia.knag.users.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	private String id;

	private String name;
	private String surname;

	@NonNull
	private String email;
	@NonNull
	private String password;

	private Role role;
	private boolean emailVerified;


	public User(String name, String surname, String email, String password, Role role, boolean emailVerified) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.role = role;
		this.emailVerified = emailVerified;
	}
}
