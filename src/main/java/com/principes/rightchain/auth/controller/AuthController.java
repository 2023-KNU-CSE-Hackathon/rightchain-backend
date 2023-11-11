package com.principes.rightchain.auth.controller;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.LoginRequestDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.auth.service.AuthService;
import com.principes.rightchain.email.EmailService;
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
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final EmailService emailService;

    @PostMapping("/register")
    public ApiSuccessResult<Long> signUp(
            @Valid @RequestBody RegisterRequestDto requestDto,
            @RequestParam("code") String code){

        if (!emailService.validCode(requestDto.getEmail(), code)) {
            throw new IllegalStateException("유효하지 않는 인증코드");
        }

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

        ResponseCookie cookie = cookieUtil.createCookie(tokenDto.getAccessToken());
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(loginResponseDto));
    }

    @GetMapping("/test")
    public ApiSuccessResult<Long> testC(Authentication authentication) {
        Account account = ((PrincipalDetails) authentication.getPrincipal()).getAccount();

        return ApiUtil.success(account.getId());
    }

    @GetMapping("/email-auth/issue")
    public ApiSuccessResult<String> issueEmailCode(
            @RequestParam("email") String email
    ) {
        emailService.emailAuthorization(email);
        return ApiUtil.success("성공적으로 이메일을 전송했습니다.");
    }

    @GetMapping("/email-auth/valid")
    public ApiSuccessResult<Boolean> isValidEmailCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        return ApiUtil.success(emailService.validCode(email, code));
    }
}