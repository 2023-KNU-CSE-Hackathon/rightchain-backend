package com.principes.rightchain.auth.controller;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.LoginRequestDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.auth.service.AuthService;
import com.principes.rightchain.security.details.PrincipalDetails;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;
import com.principes.rightchain.utils.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ApiSuccessResult<Long> signUp(@RequestBody RegisterRequestDto requestDto){
        return ApiUtil.success(authService.register(requestDto));
    }

    @PostMapping("/login")
    public ApiSuccessResult<ResponseEntity<LoginResponseDto>> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse res
    ){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        LoginResponseDto loginResponseDto = authService.login(email, password);
        TokenDto tokenDto = loginResponseDto.getTokenDto();

        String authorization = tokenDto.getGrantType() + " " + tokenDto.getAccessToken();

        ResponseCookie cookie = cookieUtil.createCookie(tokenDto.getAccessToken());
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(loginResponseDto));
    }
    @GetMapping("/test")
    public ApiSuccessResult<Account> testC(Authentication authentication) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();

        return ApiUtil.success(userDetails.account());
    }
}