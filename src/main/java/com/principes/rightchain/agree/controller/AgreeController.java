package com.principes.rightchain.agree.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.service.AgreeService;
import com.principes.rightchain.security.details.PrincipalDetails;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

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

        Long id = agreeService.agreeCreateAndDelete(account, caseNum);

        return ApiUtil.success(id);
    }
}
