package com.kerem.Repository;

import com.kerem.Constant.Status;
import com.kerem.Entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByAuthId(Long authId);

    Optional<UserProfile> findByUsername(String username);
    Optional<List<UserProfile>> findByStatus(Status status);
}
