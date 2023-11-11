package com.principes.rightchain.oauth.kakao.service;

import com.principes.rightchain.exception.NotEmailValidException;
import com.principes.rightchain.exception.NotEmailVerifiedException;
import com.principes.rightchain.exception.OauthInvalidAccessTokenException;
import com.principes.rightchain.exception.OauthInvalidAuthorizationCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoService {

    @Value("${spring.oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.oauth.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${spring.oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.oauth.kakao.user-info-uri}")
    private String userInfoUri;

    public String getKakaoEmail(String authCode) {
        String accessToken = getKakaoAccessToken(authCode);
        log.info("access Token : " + accessToken);

        Map userInfo = getKakaoUserInfo(accessToken);
        log.info("user info : " + userInfo.toString());

        Map kakaoAccount = (Map) userInfo.get("kakao_account");

        Boolean isEmailValid = (Boolean) kakaoAccount.get("is_email_valid");
        Boolean isEmailVerified = (Boolean) kakaoAccount.get("is_email_verified");

        if (!isEmailValid) throw new NotEmailValidException("유효하지 않는 이메일");
        if (!isEmailVerified) throw new NotEmailVerifiedException("인증되지 않는 이메일");

        return (String) kakaoAccount.get("email");
    }

    private String getKakaoAccessToken(String authCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", restApiKey);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", authCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new OauthInvalidAuthorizationCodeException("Kakao Service - 잘못된 인가 코드");
        }

        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
    }

    private Map getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new OauthInvalidAccessTokenException("Kakao Service - 잘못된 Access Token");
        }

        return response.getBody();
    }
}

