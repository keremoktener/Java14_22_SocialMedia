package com.kerem.Controller;

import com.kerem.Constant.EndPoints;
import com.kerem.Constant.Status;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Dto.Request.UserProfileUpdateRequestDto;
import com.kerem.Entity.UserProfile;
import com.kerem.Service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndPoints.ROOT + EndPoints.USERPROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

//    @PostMapping(EndPoints.SAVE)
//    public ResponseEntity<Void> save(@RequestBody UserProfileSaveRequestDto dto){
//        userProfileService.save(dto);
//        return ResponseEntity.ok().build();
//    }

//    @PostMapping("/activate/{authId}")
//    public ResponseEntity<Void> activateUserProfile(@PathVariable("authId") Long authId) {
//        userProfileService.activateUserProfile(authId);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateUserProfile(String token, @RequestBody UserProfileUpdateRequestDto dto) {
        userProfileService.updateUserProfile(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(EndPoints.DELETE + "/{authId}")
    public ResponseEntity<Void> softDeleteUserProfile(@PathVariable("authId") Long authId){
        userProfileService.softDeleteUserProfile(authId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(EndPoints.FINDIDBYAUTHID + "/{authId}")
    public String findIdByAuthId(@PathVariable("authId") Long authId){
        return userProfileService.findIdByAuthId(authId);
    }

    @GetMapping(EndPoints.FINDBYUSERNAMEREDIS)
    public UserProfile findByUsername(@RequestParam String username){
        return userProfileService.findByUsername(username);
    }

    @GetMapping(EndPoints.FINDBYSTATUS)
    public List<UserProfile> findByStatus(@RequestParam Status status){
        return userProfileService.findByStatus(status);
    }
}
