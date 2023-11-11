package com.principes.rightchain.chain.controller;

import com.principes.rightchain.chain.dto.request.ChainStackRequest;
import com.principes.rightchain.chain.service.ChainService;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chains")
@RequiredArgsConstructor
public class ChainController {
    private final ChainService chainService;

    @PostMapping("/stacks/{caseNum}")
    public ApiSuccessResult<String> stackChain(
            @PathVariable("caseNum") String caseNum,
            @RequestBody ChainStackRequest chainStackRequest
    ) {
        chainService.stackChain(caseNum, chainStackRequest.getWalletName());

        return ApiUtil.success("성공적으로 저장되었습니다.");
    }

    @PostMapping("/changes/{caseNum}")
    public ApiSuccessResult<String> changeProgressStatus(
            @PathVariable("caseNum") String caseNum
    ) {
        chainService.updateProgressStatus(caseNum);

        return ApiUtil.success("성공적으로 저장되었습니다.");
    }
}
