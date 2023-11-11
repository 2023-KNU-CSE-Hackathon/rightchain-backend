package com.principes.rightchain.chain.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlockChainApi {
    private final RestTemplate restTemplate;

    @Value("${spring.chain.api-token}")
    private String API_TOKEN;

    /**
     *
     * @param wallet_name 지갑 이름 설정
     * @response_body
     *
     * {
     *     "payload": {
     *         "id": "9ba035f4-1b5c-4bd9-a99c-ba87cb074363",
     *         "name": "신고접수",
     *         "address": "0x3e9ebc01135b82eedaef29935e8852566ea894a5",
     *         "privateKey": "0xf6f618f1baed5a41ab5cf61c62c626ec51f9f404e3039d2b4acba1ff45ce3521",
     *         "datetime": "2023-11-08T01:23:54+00:00",
     *         "timestamp": 1699406634
     *     },
     *     "state": {
     *         "code": 200,
     *         "success": true
     *     }
     * }
     * @return 지갑 address 주소
     */
    public String createWallet(String wallet_name) {
        final String URI = "https://testnet-api.blocksdk.com/v3/matic/address/?api_token=" + API_TOKEN;
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name", wallet_name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                URI,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException();
        }

        Map payload = (Map) Objects.requireNonNull(response.getBody()).get("payload");

        return (String) payload.get("address");
    }
}
