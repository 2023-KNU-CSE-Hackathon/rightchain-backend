package com.principes.rightchain.agree.service;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.agree.repository.AgreeRepository;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AgreeService {
    private final AgreeRepository agreeRepository;
    private final ReportService reportService;

    @Transactional
    public Long agreeReport(Account account, String caseNum) {
        Report report = reportService.findReportByCaseNum(caseNum);
        Agree agree = agreeRepository.save(Agree.builder()
                .account(account)
                .report(report).build());

        return agree.getId();
    }

    @Transactional
    public void agreeReportCancel(Account account, String caseNum) {
        Report report = reportService.findReportByCaseNum(caseNum);
        agreeRepository.deleteByAccountAndReport(account, report);
    }

}
