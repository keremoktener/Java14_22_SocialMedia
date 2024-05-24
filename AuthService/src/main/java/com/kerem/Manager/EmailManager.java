package com.kerem.Manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:9093/email", name = "emailManager")
public interface EmailManager {

    @GetMapping("/sendactivationcode")
    ResponseEntity<Void> sendActivationCode(@RequestParam String toEmail, String activationCode);
}
