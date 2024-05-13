package com.kerem.Service;

import com.kerem.Constant.Role;
import com.kerem.Constant.Status;
import com.kerem.Dto.Request.ActivateCodeRequestDto;
import com.kerem.Dto.Request.AuthLoginRequestDto;
import com.kerem.Dto.Request.AuthRegisterRequestDto;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Dto.Response.AuthRegisterResponseDto;
import com.kerem.Entity.Auth;
import com.kerem.Manager.UserProfileManager;
import com.kerem.Mapper.AuthMapper;
import com.kerem.Repository.AuthRepository;
import com.kerem.Utility.CodeGenerator;
import com.kerem.Utility.JwtTokenManager;
import com.kerem.exceptions.AuthMicroServiceException;
import com.kerem.exceptions.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserProfileManager userProfileManager;


    public AuthRegisterResponseDto save(AuthRegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRepassword())) {
            throw new AuthMicroServiceException(ErrorType.PASSWORDS_ARE_NOT_SAME);
        }
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new AuthMicroServiceException(ErrorType.USERNAME_TAKEN);
        }
        Auth auth = AuthMapper.INSTANCE.authRegisterDtoToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        authRepository.save(auth);

        UserProfileSaveRequestDto userProfileSaveRequestDto = UserProfileSaveRequestDto.builder()
                .authId(auth.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();

        userProfileManager.save(userProfileSaveRequestDto);

        return AuthMapper.INSTANCE.authToAuthRegisterResponseDto(auth);
    }

    public String doLogin(AuthLoginRequestDto dto) {
        Auth auth = findOptionalByUsernameAndPassword(dto.getUsername(),
                dto.getPassword());
        if (!auth.getStatus().equals(Status.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.USER_NOT_ACTIVE);
        }
        return createTokenWithIdAndRole(auth.getId());
    }

    public Auth findOptionalByUsernameAndPassword(String username, String password) {
        return authRepository.findOptionalByUsernameAndPassword(username,
                        password)
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));

    }

    public String activateAccount(ActivateCodeRequestDto dto) {
        Auth auth = authRepository.findById(dto.getId())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (auth.getStatus().equals(Status.PENDING)) {
            if (!auth.getActivationCode().equals(dto.getCode())) {
                throw new AuthMicroServiceException(ErrorType.ACTIVATION_CODE_NOT_FOUND);
            }
            auth.setStatus(Status.ACTIVE);
            authRepository.save(auth);
            return jwtTokenManager.createToken(auth.getId()).get();
        } else if (auth.getStatus().equals(Status.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.USER_ALREADY_ACTIVE);
        } else if (auth.getStatus().equals(Status.BANNED)) {
            throw new AuthMicroServiceException(ErrorType.USER_IS_BANNED);
        } else {
            throw new AuthMicroServiceException(ErrorType.USER_DELETED);
        }
    }

    public String softDelete(Long authId) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (!auth.getStatus().equals(Status.DELETED)) {
            auth.setStatus(Status.DELETED);
            authRepository.save(auth);
            return "User with id " + authId + " has been deleted";
        } else {
            throw new AuthMicroServiceException(ErrorType.USER_ALREADY_DELETED);
        }
    }

    public String createTokenWithId(Long id) {
        return jwtTokenManager.createToken(id).orElseThrow(() -> new AuthMicroServiceException(ErrorType.ID_NOT_FOUND));
    }

    public String createTokenWithIdAndRole(Long id) {
        Auth auth = authRepository.findById(id).orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        Role role = auth.getRole();
        Optional<String> token = jwtTokenManager.createTokenWithIdAndRole(id, role);
        if (token.isPresent()) {
            return token.get();
        } else {
            throw new AuthMicroServiceException(ErrorType.ID_NOT_FOUND);
        }
    }

    public Long getIdFromToken(String token) {
        return jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new AuthMicroServiceException(ErrorType.INVALID_TOKEN));
    }

    public String getRoleFromToken(String token) {
        Role role = jwtTokenManager.getRoleFromToken(token).orElseThrow(() -> new AuthMicroServiceException(ErrorType.INVALID_TOKEN));
        return role.toString();
    }

}
