package com.kerem.Mapper;

import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Entity.UserProfile;
import com.kerem.Service.UserProfileService;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileSaveRequestDto userProfileToDto(UserProfile userProfile);
    UserProfile userProfileSaveRequestDtoToUserProfile(UserProfileSaveRequestDto userProfileSaveRequestDto);
}
