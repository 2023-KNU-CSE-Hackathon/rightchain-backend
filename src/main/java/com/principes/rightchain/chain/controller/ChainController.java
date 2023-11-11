package com.principes.rightchain.chain.controller;

import com.principes.rightchain.chain.service.ChainService;
import com.principes.rightchain.utils.api.ApiUtil.*;
import com.principes.rightchain.utils.api.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chains")
@RequiredArgsConstructor
public class ChainController {
    private final ChainService chainService;

    @PostMapping("/{caseNum}")
    public ApiSuccessResult<String> stackChain(
            @PathVariable("caseNum") String caseNum,
            @RequestParam("wallet-name") String walletName
    ) {
        chainService.stackChain(caseNum, walletName);

        return ApiUtil.success("성공적으로 저장되었습니다.");
    }


    @PostMapping("/{caseNum}")
    public ApiSuccessResult<String> changeProgressStatus(
            @PathVariable("caseNum") String caseNum
    ) {
        chainService.updateProgressStatus(caseNum);

        return ApiUtil.success("성공적으로 저장되었습니다.");
    }



}
