package com.kerem.Service;

import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Entity.UserProfile;
import com.kerem.Mapper.UserProfileMapper;
import com.kerem.Repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public void save(UserProfileSaveRequestDto dto){
//        userProfileRepository.save(
//                UserProfile.builder()
//                        .authId(dto.getAuthId())
//                        .username(dto.getUsername())
//                        .email(dto.getEmail())
//                        .phone(dto.getPhone())
//                        .photo(dto.getPhoto())
//                        .address(dto.getAddress())
//                        .about(dto.getAbout())
//                        .build()
//        );

        UserProfile userProfile = UserProfileMapper.INSTANCE.userProfileSaveRequestDtoToUserProfile(dto);
        userProfileRepository.save(userProfile);
    }

}
