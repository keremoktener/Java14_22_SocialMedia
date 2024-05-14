package com.kerem.Manager;

import com.kerem.Constant.EndPoints;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9093/api/v1/userprofile", name = "userProfileManager")
public interface UserProfileManager {
    @PostMapping(EndPoints.SAVE)
    ResponseEntity<Void> save(@RequestBody UserProfileSaveRequestDto dto);

    @PostMapping("/activate/{authId}")
    ResponseEntity<Void> activateUserProfile(@PathVariable("authId") Long authId);

    @DeleteMapping(EndPoints.DELETE + "/{authId}")
    ResponseEntity<Void> softDeleteUserProfile(@PathVariable("authId") Long authId);
}
