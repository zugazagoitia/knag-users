package com.zugazagoitia.knag.users.repositories;

import com.zugazagoitia.knag.users.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

}
