package com.kerem.Controller;

import com.kerem.Constant.EndPoints;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndPoints.ROOT + EndPoints.USERPROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(EndPoints.SAVE)
    public ResponseEntity<Void> save(@RequestBody UserProfileSaveRequestDto dto){
        userProfileService.save(dto);
        return ResponseEntity.ok().build();
    }
}
