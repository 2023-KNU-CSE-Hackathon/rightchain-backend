package com.principes.rightchain.chain.service;

import com.principes.rightchain.chain.component.BlockChainApi;
import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.chain.entity.ProgressStatus;
import com.principes.rightchain.chain.repository.ChainRepository;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChainService {
    private final ChainRepository chainRepository;
    private final ReportService reportService;
    private final BlockChainApi blockChainApi;

    @Transactional
    public void createChain(Report report, String walletName) {
        String address = blockChainApi.createWallet(walletName);

        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .progressStatus(ProgressStatus.REPORT_SUBMITTED)
                        .report(report)
                        .build());
    }

    @Transactional
    public void stackChain(String caseNum, String walletName) {
        Report report = reportService.findReportByCaseNum(caseNum);

        ProgressStatus currentProgressStatus = chainRepository.findProgressStatusByCaseId(report.getId());

        String address = blockChainApi.createWallet(walletName);
        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .progressStatus(currentProgressStatus)
                        .report(report)
                        .build());
    }

    @Transactional
    public void updateProgressStatus(String caseNum) {
        Report report = reportService.findReportByCaseNum(caseNum);

        ProgressStatus currentProgressStatus = chainRepository.findProgressStatusByCaseId(report.getId());

        if (currentProgressStatus.ordinal() >= ProgressStatus.values().length) {
            throw new RuntimeException("마지막 PrgressStatus에서 업데이트를 시도했습니다.");
        }

        ProgressStatus updatedProgressStatus = ProgressStatus.values()[currentProgressStatus.ordinal() + 1];

        String enumDec = ProgressStatus.getProgressStatusDescription(updatedProgressStatus);

        String address = blockChainApi.createWallet(enumDec);
        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .progressStatus(updatedProgressStatus)
                        .report(report)
                        .build());

    }
}
