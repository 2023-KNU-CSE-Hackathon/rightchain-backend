package com.principes.rightchain.oauth.kakao.controller;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.service.AccountService;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.oauth.kakao.service.KakaoService;
import com.principes.rightchain.oauth.oauth2.service.OAuthService;
import com.principes.rightchain.utils.api.ApiUtil.*;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/oauth/kakao")
@RequiredArgsConstructor
public class KakaoController {
    private final CookieUtil cookieUtil;
    private final KakaoService kakaoService;
    private final OAuthService oAuthService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ApiSuccessResult<?> kakaoLogin(
            @RequestParam("code") String authCode,
            HttpServletResponse res) throws RuntimeException {

        String email = kakaoService.getKakaoEmail(authCode);

        if (accountService.isAccountByEmail(email)) {
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
    public ApiSuccessResult<Long> signUp(@RequestBody RegisterRequestDto requestDto){
        return ApiUtil.success(oAuthService.oRegister(requestDto));
    }

}
