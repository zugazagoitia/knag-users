package com.zugazagoitia.knag.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection="UsersCollection")
public class User extends PanacheMongoEntity {
	private String name;
	private Instant creationTime;
	private String email;
	private String ip;
	private String password;
}
