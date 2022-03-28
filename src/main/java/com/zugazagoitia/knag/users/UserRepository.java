package com.zugazagoitia.knag.users;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "v1/users")
public interface UserRepository extends MongoRepository<User, String> {

	List<User> findByName(@Param("name") String name);
	User findByEmail(@Param("email") String email);
	User findUserById(@Param("username") String username);
	List<User> findBySubscription(@Param("subscription") String subscription);

}
