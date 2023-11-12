package com.principes.rightchain.auth.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.LoginRequestDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.auth.dto.response.MeResponseDto;
import com.principes.rightchain.auth.service.AuthService;
import com.principes.rightchain.email.EmailService;
import com.principes.rightchain.security.details.PrincipalDetails;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ApiSuccessResult<Long> signUp(
            @Valid @RequestBody RegisterRequestDto requestDto
    ){

        return ApiUtil.success(authService.register(requestDto));
    }

    @PostMapping("/login")
    public ApiSuccessResult<ResponseEntity<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse res
    ){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        LoginResponseDto loginResponseDto = authService.login(email, password);

        TokenDto tokenDto = loginResponseDto.getTokenDto();

        String authorization = tokenDto.getGrantType() + " " + tokenDto.getAccessToken();

        return ApiUtil.success(ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(loginResponseDto));
    }

    @GetMapping("/info")
    public ApiSuccessResult<MeResponseDto> userInfo(Authentication authentication) {
        Account account = ((PrincipalDetails) authentication.getPrincipal()).getAccount();

        return ApiUtil.success(MeResponseDto.builder()
                .email(account.getEmail())
                .name(account.getName())
                .role(account.getRole())
                .schoolName(account.getSchoolName())
                .build());
    }

    @GetMapping("/email-auth/issue")
    public ApiSuccessResult<String> issueEmailCode(
            @RequestParam("email") String email
    ) {
        emailService.emailAuthorization(email);
        return ApiUtil.success("성공적으로 이메일을 전송했습니다.");
    }

    @GetMapping("/email-auth/valid")
    public ApiSuccessResult<String> isValidEmailCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        emailService.validCode(email, code);
        return ApiUtil.success("인증되었습니다!");
    }

    @GetMapping("/valid-email")
    public ApiSuccessResult<String> isValidEmail(
            @RequestParam("email") String email
    ) {
        if (authService.isAccountByEmail(email)) {
            throw new IllegalStateException("중복된 이메일 입니다.");
        }
        return ApiUtil.success("사용 가능한 이메일 입니다.");
    }
}