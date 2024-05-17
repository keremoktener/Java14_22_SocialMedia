package com.kerem.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kerem.Constant.Status;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Dto.Request.UserProfileUpdateRequestDto;
import com.kerem.Entity.UserProfile;
import com.kerem.Mapper.UserProfileMapper;
import com.kerem.Repository.UserProfileRepository;
import com.kerem.Utility.JwtTokenManager;
import com.kerem.exceptions.ErrorType;
import com.kerem.exceptions.UserProfileMicroServiceException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;

//    public void save(UserProfileSaveRequestDto dto){
//
//        UserProfile userProfile = UserProfileMapper.INSTANCE.userProfileSaveRequestDtoToUserProfile(dto);
//        userProfileRepository.save(userProfile);
//    }

//    @Transactional
//    public void activateUserProfile(Long authId) {
//        UserProfile userProfile = userProfileRepository.findByAuthId(authId)
//                .orElseThrow(() -> new UserProfileMicroServiceException(ErrorType.KULLANICI_NOT_FOUND));
//
//        userProfile.setStatus(Status.ACTIVE);
//        userProfileRepository.save(userProfile);
//    }

    public void updateUserProfile(String token, UserProfileUpdateRequestDto dto) {
        Long authId = jwtTokenManager.getIdFromToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        UserProfile userProfile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserProfileMicroServiceException(ErrorType.KULLANICI_NOT_FOUND));

        userProfile.setEmail(dto.getEmail());
        userProfile.setPhone(dto.getPhoto());
        userProfile.setPhoto(dto.getPhoto());
        userProfile.setAddress(dto.getAddress());
        userProfile.setAbout(dto.getAbout());

        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void softDeleteUserProfile(Long authId){
        UserProfile userProfile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserProfileMicroServiceException(ErrorType.KULLANICI_NOT_FOUND));

        userProfile.setStatus(Status.DELETED);
        userProfileRepository.save(userProfile);
    }

    public String findIdByAuthId(Long authId){
        Optional<UserProfile> byAuthId = userProfileRepository.findByAuthId(authId);
        if (byAuthId.isEmpty()){
            throw new UserProfileMicroServiceException(ErrorType.KULLANICI_NOT_FOUND);
        } else {
            UserProfile userProfile = byAuthId.get();
            return userProfile.getId();
        }
    }

}
