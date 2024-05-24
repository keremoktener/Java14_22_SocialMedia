package com.kerem.Service;

import com.kerem.Constant.Role;
import com.kerem.Constant.Status;
import com.kerem.Controller.ProducerController;
import com.kerem.Dto.Request.*;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserProfileManager userProfileManager;
    private final ProducerController producerController;
    private final RabbitTemplate rabbitTemplate;


//    public AuthRegisterResponseDto save(AuthRegisterRequestDto dto) {
//        if (!dto.getPassword().equals(dto.getRepassword())) {
//            throw new AuthMicroServiceException(ErrorType.PASSWORDS_ARE_NOT_SAME);
//        }
//        if (authRepository.existsByUsername(dto.getUsername())) {
//            throw new AuthMicroServiceException(ErrorType.USERNAME_TAKEN);
//        }
//        Auth auth = AuthMapper.INSTANCE.authRegisterDtoToAuth(dto);
//        auth.setActivationCode(CodeGenerator.generateCode());
//        authRepository.save(auth);
//
//        userProfileManager.save(AuthMapper.INSTANCE.toDto(auth));
//
//        return AuthMapper.INSTANCE.authToAuthRegisterResponseDto(auth);
//    }

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

        producerController.convertAndSend(dto);

        ActivationCodeEmailRequestDto activationCodeEmailRequestDto = new ActivationCodeEmailRequestDto();
        activationCodeEmailRequestDto.setToEmail(dto.getEmail());
        activationCodeEmailRequestDto.setActivationCode(auth.getActivationCode());
        sendRegistrationEmail(activationCodeEmailRequestDto);

        return AuthMapper.INSTANCE.reqToResponse(dto);

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


    @Transactional
    public String activateAccount(ActivateCodeRequestDto dto) {
        Auth auth = authRepository.findById(dto.getId())
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (auth.getStatus().equals(Status.PENDING)) {
            if (!auth.getActivationCode().equals(dto.getCode())) {
                throw new AuthMicroServiceException(ErrorType.ACTIVATION_CODE_NOT_FOUND);
            }
            auth.setStatus(Status.ACTIVE);
            authRepository.save(auth);

            try {
//                userProfileManager.activateUserProfile(auth.getId());
                producerController.activateAccountWithRabbit(auth.getId());
            } catch (Exception e) {
                throw new RuntimeException("Failed to activate user profile", e);
            }

            return jwtTokenManager.createToken(auth.getId()).get();
        } else if (auth.getStatus().equals(Status.ACTIVE)) {
            throw new AuthMicroServiceException(ErrorType.USER_ALREADY_ACTIVE);
        } else if (auth.getStatus().equals(Status.BANNED)) {
            throw new AuthMicroServiceException(ErrorType.USER_IS_BANNED);
        } else {
            throw new AuthMicroServiceException(ErrorType.USER_DELETED);
        }
    }

    @Transactional
    public String softDelete(Long authId) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        if (!auth.getStatus().equals(Status.DELETED)) {
            auth.setStatus(Status.DELETED);
            authRepository.save(auth);
            try {
                userProfileManager.softDeleteUserProfile(authId);
            } catch (Exception e){
                throw new AuthMicroServiceException(ErrorType.BAD_REQUEST);
            }
            return "User with id " + authId + " has been deleted";
        } else {
            throw new AuthMicroServiceException(ErrorType.USER_ALREADY_DELETED);
        }
    }

    public void forgotPassword(String email){
        Auth auth = authRepository.findByEmail(email)
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));

        String newActivationCode = CodeGenerator.generateCode();

        auth.setActivationCode(newActivationCode);

        ActivationCodeEmailRequestDto dto = new ActivationCodeEmailRequestDto();
        dto.setToEmail(dto.getToEmail());
        dto.setActivationCode(auth.getActivationCode());
        sendRegistrationEmail(dto);
    }

    public void resetPassword(String activationCode, String newPassword){
        Auth auth = authRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));

        auth.setPassword(newPassword);
        authRepository.save(auth);

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

    public Boolean updateMail(Long id, String email) {
        Auth auth = authRepository.findById(id).orElseThrow(() -> new AuthMicroServiceException(ErrorType.USER_NOT_FOUND));
        auth.setEmail(email);
        authRepository.save(auth);
        return true;
    }

    public void sendRegistrationEmail(ActivationCodeEmailRequestDto dto) {
        rabbitTemplate.convertAndSend("q.C", dto);
    }
}
