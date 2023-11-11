package com.principes.rightchain.agree.controller;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.service.AgreeService;
import com.principes.rightchain.security.details.PrincipalDetails;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agrees")
@RequiredArgsConstructor
public class AgreeController {
    private final AgreeService agreeService;

    @PostMapping("/{caseNum}")
    public ApiSuccessResult<Long> agreeReports(
            Authentication authentication,
            @PathVariable("caseNum") String caseNum) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        Long id = agreeService.agreeReport(account, caseNum);

        return ApiUtil.success(id);
    }

    @DeleteMapping("/{caseNum}")
    public void agreeReportsCancel(
            Authentication authentication,
            @PathVariable("caseNum") String caseNum) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        agreeService.agreeReportCancel(account, caseNum);
    }
}
