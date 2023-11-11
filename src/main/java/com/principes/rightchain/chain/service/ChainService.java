package com.principes.rightchain.chain.service;

import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.chain.entity.ProgressStatus;
import com.principes.rightchain.chain.repository.ChainRepository;
import com.principes.rightchain.exception.NotFoundException;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.repository.ReportRepository;
import com.principes.rightchain.wallet.component.BlockChainApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChainService {
    private final ChainRepository chainRepository;
    private final ReportRepository reportRepository;
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
        Report report = this.findReportByCaseNum(caseNum);

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
        Report report = this.findReportByCaseNum(caseNum);

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

    private Report findReportByCaseNum(String caseNum) {
        return reportRepository.findById(getIdFromCaseNum(caseNum))
                .orElseThrow(() -> new NotFoundException("해당 사건 번호로 사건을 조회할 수 없습니다."));
    }

    private Long getIdFromCaseNum(String caseNum) {
        return Long.parseLong(caseNum) % 10000;
    }

}
