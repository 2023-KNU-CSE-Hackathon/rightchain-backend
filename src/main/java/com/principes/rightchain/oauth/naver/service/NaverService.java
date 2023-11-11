package com.principes.rightchain.oauth.naver.service;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverService {

    @Value("${spring.oauth.naver.token-uri}")
    private String tokenUri;

    @Value("${spring.oauth.naver.rest-api-key}")
    private String restApiKey;

    @Value("${spring.oauth.naver.redirect-uri}")
    private String redirectUri;

    @Value("${spring.oauth.naver.user-info-uri}")
    private String userInfoUri;
    public String getNaverEmail(String authCode) {
        String accessToken = getNaverAccessToken(authCode);
        log.info("access Token : " + accessToken);

        Map userInfo = getNaverUserInfo(accessToken);
        log.info("user info : " + userInfo.toString());

        Map response = (Map) userInfo.get("response");

        return (String) response.get("email");
    }

    private String getNaverAccessToken(String authCode) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("client_id", restApiKey);
        requestBody.put("client_secret", redirectUri);
        requestBody.put("state", "RightChain");
        requestBody.put("code", authCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new OauthInvalidAuthorizationCodeException("Naver Service - 잘못된 인가 코드");
        }

        return (String) Objects.requireNonNull(response.getBody()).get("accessToken");
    }

    private Map getNaverUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new OauthInvalidAccessTokenException("Naver Service - 잘못된 Access Token");
        }

        return response.getBody();
    }

    /*
    {
	"resultcode": "00",
	"message": "success",
	"response": {
		"email": "openapi@naver.com",
		"nickname": "OpenAPI",
		"profile_image": "https://ssl.plastic.net/static/pwe/address/nodata_33x33.gif",
		"age": "40-49",
		"gender": "F",
		"id": "33742276",
		"name": "오픈 API",
		"birthday": "10-01"
	}
}
     */
}

