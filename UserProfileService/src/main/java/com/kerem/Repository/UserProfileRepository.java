package com.kerem.Repository;

import com.kerem.Entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, Long> {

    Optional<UserProfile> findByAuthId(Long authId);

}
