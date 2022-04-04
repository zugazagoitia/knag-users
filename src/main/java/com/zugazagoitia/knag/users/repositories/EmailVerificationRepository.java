package com.zugazagoitia.knag.users.repositories;

import com.zugazagoitia.knag.users.model.EmailVerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends MongoRepository<EmailVerificationToken, String> {

}

