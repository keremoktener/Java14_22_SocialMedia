package com.kerem.Mapper;

import com.kerem.Dto.Request.AuthRegisterRequestDto;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Dto.Response.AuthRegisterResponseDto;
import com.kerem.Entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    AuthRegisterRequestDto authToAuthRegisterDto(Auth auth);
    Auth authRegisterDtoToAuth(AuthRegisterRequestDto authRegisterDto);

    AuthRegisterResponseDto authToAuthRegisterResponseDto(Auth auth);

    @Mapping(source = "id", target = "authId")
    UserProfileSaveRequestDto toDto(Auth auth);
//    AuthLoginResponseDto authToAuthLoginDto(Auth auth);
//    AuthRegisterResponseDto authToAuthRegisterResponseDto(Auth auth);
}
