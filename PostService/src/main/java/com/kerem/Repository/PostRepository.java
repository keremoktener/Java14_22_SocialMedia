package com.kerem.Repository;


import com.kerem.Entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, Long> {
    Optional<List<Post>> findAllById(String id);
}
