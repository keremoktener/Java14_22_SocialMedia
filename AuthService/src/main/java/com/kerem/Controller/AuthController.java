package com.kerem.Controller;

import com.kerem.Constant.EndPoints;
import com.kerem.Dto.Request.ActivateCodeRequestDto;
import com.kerem.Dto.Request.AuthRegisterRequestDto;
import com.kerem.Dto.Request.AuthLoginRequestDto;
import com.kerem.Dto.Response.AuthRegisterResponseDto;
import com.kerem.Entity.Auth;
import com.kerem.Service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndPoints.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(EndPoints.REGISTER)
    public ResponseEntity<AuthRegisterResponseDto> register(@Valid @RequestBody AuthRegisterRequestDto authRegisterDto) {
        return ResponseEntity.ok(authService.save(authRegisterDto));
    }

    @PostMapping(EndPoints.LOGIN)
    public ResponseEntity<String> dologin(@RequestBody AuthLoginRequestDto dto) {
        return ResponseEntity.ok(authService.doLogin(dto));
    }

    @PutMapping(EndPoints.ACTIVATE)
    public ResponseEntity<String> activateAccount(@RequestBody ActivateCodeRequestDto dto) {
        return ResponseEntity.ok(authService.activateAccount(dto));
    }

    @DeleteMapping(EndPoints.DELETE + "/{id}")
    public ResponseEntity<String> softDeleteAccount(@PathVariable("id") Long authId) {
        return ResponseEntity.ok(authService.softDelete(authId));
    }

    @GetMapping(EndPoints.CREATETOKENFROMID + "/{id}")
    public ResponseEntity<String> createTokenFromId(@PathVariable Long id){
        String tokenWithId = authService.createTokenWithId(id);
        return ResponseEntity.ok(tokenWithId);
    }

    @GetMapping(EndPoints.CREATETOKENFROMIDANDROLE + "/{id}")
    public ResponseEntity<String> createTokenFromIdAndRole(@PathVariable Long id){
        String tokenWithIdAndRole = authService.createTokenWithIdAndRole(id);
        return ResponseEntity.ok(tokenWithIdAndRole);
    }

    @GetMapping(EndPoints.GETIDFROMTOKEN)
    public ResponseEntity<Long> getIdFromToken(String token){
        Long idFromToken = authService.getIdFromToken(token);
        return ResponseEntity.ok(idFromToken);
    }

    @GetMapping(EndPoints.GETROLEFROMTOKEN)
    public ResponseEntity<String> getRoleFromToken(String token){
        String roleFromToken = authService.getRoleFromToken(token);
        return ResponseEntity.ok(roleFromToken);
    }
}
