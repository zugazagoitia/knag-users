package com.zugazagoitia.knag.users.services.crypto;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.zugazagoitia.knag.users.model.SessionToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service("jwtProvider")
public class JwtProvider {

	private final Algorithm algorithmRS;


	public JwtProvider(@Value("#{keySettings.priv}") String rawPrivateKey, @Value("#{keySettings.pub}") String rawPubKey) {

		RSAPrivateKey privateKey = (RSAPrivateKey) KeyHelper.loadPrivateKeyBase64(rawPrivateKey);
		RSAPublicKey publicKey = (RSAPublicKey) KeyHelper.loadPublicKeyBase64(rawPubKey);

		algorithmRS = Algorithm.RSA512(publicKey, privateKey);
	}

	public String jwtFromSessionToken(SessionToken sessionToken) throws JWTCreationException {

		//TODO add roles
		return JWT.create()
				.withClaim("userid", sessionToken.getUser().getId())
				.withClaim("email", sessionToken.getUser().getEmail())
				//.withClaim("subscription",sessionToken.getUser().getId())
				.withIssuedAt(Date.from(sessionToken.getCreation()))
				.withExpiresAt(Date.from(sessionToken.getExpiration()))
				.withSubject(sessionToken.getUser().getId())
				.withJWTId(sessionToken.getToken())
				.withIssuer("knag-users")
				.sign(algorithmRS);


	}

	private static class KeyHelper {

		private static final String PRIVATE_HEADER = "-----BEGIN PRIVATE KEY-----";
		private static final String PRIVATE_FOOTER = "-----END PRIVATE KEY-----";
		private static final String PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----";
		private static final String PUBLIC_FOOTER = "-----END PUBLIC KEY-----";

		public static PrivateKey loadPrivateKeyBase64(String key) {
			PrivateKey pvt = null;
			try {
				key = key.replace(PRIVATE_HEADER, "");
				key = key.replace(PRIVATE_FOOTER, "");
				key = key.replace("\n","");

				byte[] bytes = Base64.getDecoder().decode(key.getBytes());

				/* Generate private key. */
				PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				pvt = kf.generatePrivate(ks);
			} catch (Exception x) {
				x.printStackTrace();
			}
			return pvt;
		}

		public static PublicKey loadPublicKeyBase64(String key) {
			PublicKey pub = null;
			try {
				key = key.replace(PUBLIC_HEADER, "");
				key = key.replace(PUBLIC_FOOTER, "");
				key = key.replace("\n","");

				byte[] bytes = Base64.getDecoder().decode(key.getBytes());

				/* Generate public key. */
				X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				pub = kf.generatePublic(ks);
			} catch (Exception x) {
				x.printStackTrace();
			}
			return pub;
		}


	}
}

