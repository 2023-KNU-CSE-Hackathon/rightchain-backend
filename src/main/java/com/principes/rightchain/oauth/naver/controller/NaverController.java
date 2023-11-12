package com.principes.rightchain.oauth.naver.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.principes.rightchain.account.service.AccountService;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.oauth.naver.service.NaverService;
import com.principes.rightchain.oauth.oauth2.service.OAuthService;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;
import com.principes.rightchain.utils.cookie.CookieUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/oauth/naver")
@RequiredArgsConstructor
public class NaverController {
    private final CookieUtil cookieUtil;
    private final NaverService naverService;
    private final OAuthService oAuthService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ApiSuccessResult<?> naverLogin(
            @RequestParam("code") String authCode,
            HttpServletResponse res) throws RuntimeException {

        String email = naverService.getNaverEmail(authCode);

        if (!accountService.isAccountByEmail(email)) {
            res.setStatus(401);
            return ApiUtil.success(email);
        }

        LoginResponseDto loginResponseDto = oAuthService.oLogin(email);

        TokenDto tokenDto = loginResponseDto.getTokenDto();
        String authorization = tokenDto.getGrantType() + " " + tokenDto.getAccessToken();

        ResponseCookie cookie = cookieUtil.createCookie(tokenDto.getAccessToken());
        res.addHeader("Set-Cookie", cookie.toString());

        return ApiUtil.success(ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(loginResponseDto));
    }

    @PostMapping("/register")
    public ApiSuccessResult<Long> naverRegister(@RequestBody RegisterRequestDto requestDto){
        return ApiUtil.success(oAuthService.oRegister(requestDto));
    }

}
