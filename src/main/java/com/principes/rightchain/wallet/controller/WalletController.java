package com.principes.rightchain.wallet.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;
import com.principes.rightchain.wallet.component.BlockChainApi;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final BlockChainApi blockChainApi;

    @GetMapping
    public ApiSuccessResult<Map> readWalletByAddress(
            @RequestParam("address") String address
    ) {
        Map walletInfo = blockChainApi.readWallet(address);
        if (walletInfo == null) {
            throw new IllegalStateException("조회 실패!");
        }

        return ApiUtil.success(blockChainApi.readWallet(address));
    }
}
