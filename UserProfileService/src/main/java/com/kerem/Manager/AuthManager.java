package com.kerem.Manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9092", name = "auth-manager")
public interface AuthManager {

    @PutMapping("/updatemail/{id}")
    ResponseEntity<Boolean> updateMail(@PathVariable Long id, @RequestBody String email);
}
