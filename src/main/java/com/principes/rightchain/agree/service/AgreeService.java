package com.principes.rightchain.agree.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.agree.repository.AgreeRepository;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AgreeService {
    private final AgreeRepository agreeRepository;
    private final ReportService reportService;

    @Transactional
    public Long agreeCreateAndDelete(Account account, String caseNum) {
        Report report = reportService.findReportByCaseNum(caseNum);
        if (agreeRepository.existsByAccountAndReport(account, report)) {
            agreeRepository.deleteByAccountAndReport(account, report);
            return -1L;
        }

        Agree agree = agreeRepository.save(Agree.builder()
                .account(account)
                .report(report).build());

        return agree.getId();
    }

    public List<Agree> findAllByReport(Report report) {
        return agreeRepository.findAllByReport(report);
    }


}
