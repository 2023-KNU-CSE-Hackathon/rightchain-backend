package com.principes.rightchain.wallet.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlockChainApi {

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
        final String URI = "https://testnet-api.blocksdk.com/v3/matic/address?api_token=" + API_TOKEN;
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name", wallet_name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
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




    /**
     *
     * @param address 지갑 주소
     * @return 지갑 정보 반환
     *
     * @response_body
     *
     * {
     *     "payload": {
     *         "data": [
     *             {
     *                 "id": "8f24d10d-6188-492c-8fcc-c81a6e399354",
     *                 "name": "신고접수2",
     *                 "address": "0x7cc22bf1807e7e90101b1e4f5784988264314088",
     *                 "datetime": "2023-11-11T02:46:44+00:00",
     *                 "timestamp": 1699670804
     *             },
     *             {
     *                 "id": "9ba035f4-1b5c-4bd9-a99c-ba87cb074363",
     *                 "name": "신고접수",
     *                 "address": "0x3e9ebc01135b82eedaef29935e8852566ea894a5",
     *                 "datetime": "2023-11-08T01:23:54+00:00",
     *                 "timestamp": 1699406634
     *             },
     *             {
     *                 "id": "f3191b5a-e303-4314-8820-efeb5d8c9d21",
     *                 "name": "test",
     *                 "address": "0xbfb0f50e5da8109eee21cbce4921c3c352c52ecc",
     *                 "datetime": "2023-11-06T12:45:20+00:00",
     *                 "timestamp": 1699274720
     *             }
     *         ],
     *         "total": 1
     *     },
     *     "state": {
     *         "code": 200,
     *         "success": true
     *     }
     * }
     */
    public Map readWallet(String address) {
        final int LIMIT = 100;
        final String URI = "https://testnet-api.blocksdk.com/v3/matic/address?api_token="
                + API_TOKEN + "&offset=0&limit=" + LIMIT + "&order_direction=desc";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                URI,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException();
        }

        Map payload = (Map) Objects.requireNonNull(response.getBody()).get("payload");
        List<Map> dataList = (List<Map>) payload.get("data");

        for (Map data : dataList) {
            String data_address = (String) data.get("address");
            if (data_address.equals(address)) {
                return data;
            }
        }

        return null;
    }
}
