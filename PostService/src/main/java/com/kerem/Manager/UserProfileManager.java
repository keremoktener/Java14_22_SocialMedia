package com.kerem.Manager;

import com.kerem.Constant.EndPoints;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:9093/api/v1/userprofile", name = "userProfileManagerPost")
public interface UserProfileManager {

    @GetMapping(EndPoints.FINDIDBYAUTHID + "/{authId}")
    String findIdByAuthId(@PathVariable("authId") Long authId);
}
