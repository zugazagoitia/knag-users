package com.zugazagoitia.knag.users.repositories;

import com.zugazagoitia.knag.users.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends MongoRepository<User, String> {

	List<User> findByName(@Param("name") String name);

	Optional<User> findByEmail(@Param("email") String email);

	boolean existsByEmail(String email);
}
