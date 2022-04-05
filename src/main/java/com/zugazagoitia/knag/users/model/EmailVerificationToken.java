package com.zugazagoitia.knag.users.model;


import com.zugazagoitia.knag.users.services.crypto.TokenGenerator;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
public class EmailVerificationToken {

	private User user;

	@MongoId
	private String token;

	public EmailVerificationToken(User user){
		this.user=user;
		token= TokenGenerator.generateNewToken(TokenGenerator.TokenType.EMAIL);
	}
}
