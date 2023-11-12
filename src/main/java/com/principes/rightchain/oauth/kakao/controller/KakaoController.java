package com.principes.rightchain.oauth.kakao.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
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
import com.principes.rightchain.oauth.kakao.service.KakaoService;
import com.principes.rightchain.oauth.oauth2.service.OAuthService;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/oauth/kakao")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final OAuthService oAuthService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ApiSuccessResult<?> kakaoLogin(
            @RequestParam("code") String authCode,
            HttpServletResponse res) throws RuntimeException {

        String email = kakaoService.getKakaoEmail(authCode);
        log.info(email);
        
        if (!accountService.isAccountByEmail(email)) {
            res.setStatus(401);
            return ApiUtil.success(email);
        }

        LoginResponseDto loginResponseDto = oAuthService.oLogin(email);

        TokenDto tokenDto = loginResponseDto.getTokenDto();
        String authorization = tokenDto.getGrantType() + " " + tokenDto.getAccessToken();

        return ApiUtil.success(ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(loginResponseDto));
    }

    @PostMapping("/register")
    public ApiSuccessResult<Long> kakaoRegister(@RequestBody RegisterRequestDto requestDto){
        return ApiUtil.success(oAuthService.oRegister(requestDto));
    }

}
