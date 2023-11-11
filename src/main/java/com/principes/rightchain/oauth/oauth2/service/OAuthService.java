package com.principes.rightchain.oauth.oauth2.service;

import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {
    private final AuthService authService;

    @Value("${spring.oauth.password}")
    private String password;


    public LoginResponseDto oLogin(String email){
        return authService.login(email, password);
    }

    @Transactional
    public Long oRegister(RegisterRequestDto requestDto){
        requestDto.setPassword(password);
        return authService.register(requestDto);
    }
}
