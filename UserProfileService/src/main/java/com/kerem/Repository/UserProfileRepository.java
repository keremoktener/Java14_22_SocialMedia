package com.kerem.Repository;

import com.kerem.Entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile, Long> {

}
