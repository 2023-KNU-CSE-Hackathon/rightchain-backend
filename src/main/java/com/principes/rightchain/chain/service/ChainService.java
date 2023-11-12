package com.principes.rightchain.chain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.chain.entity.ProgressStatus;
import com.principes.rightchain.chain.repository.ChainRepository;
import com.principes.rightchain.exception.NotFoundException;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.repository.ReportRepository;
import com.principes.rightchain.wallet.component.BlockChainApi;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChainService {
    private final ChainRepository chainRepository;
    private final ReportRepository reportRepository;
    private final BlockChainApi blockChainApi;

    private final String ENTRY_POINT = "단계 종결";

    @Transactional
    public void createChain(Report report) {
        String address = blockChainApi.createWallet("[1] 신고 접수");

        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .walletName("[1] 신고 접수")
                        .progressStatus(ProgressStatus.REPORT_SUBMITTED)
                        .report(report)
                        .build());
    }

    @Transactional
    public void stackChain(String caseNum, String walletName) {
        Report report = this.findReportByCaseNum(caseNum);
        ProgressStatus currentProgressStatus = getCurrentProgress(report);
        
        if (walletName.equals(ENTRY_POINT)) {
            updateProgressStatus(report, currentProgressStatus);
        } else {
            String address = blockChainApi.createWallet(walletName);

            chainRepository.save(
                    Chain.builder()
                            .address(address)
                            .walletName(walletName)
                            .progressStatus(currentProgressStatus)
                            .report(report)
                            .build());
        }

    }

    @Transactional
    public void updateProgressStatus(Report report, ProgressStatus currentProgressStatus) {
        if (currentProgressStatus.ordinal() >= ProgressStatus.values().length) {
            throw new RuntimeException("마지막 PrgressStatus에서 업데이트를 시도했습니다.");
        }

        ProgressStatus updatedProgressStatus = ProgressStatus.values()[currentProgressStatus.ordinal() + 1];

        String walletName = ProgressStatus.getProgressStatusDescription(updatedProgressStatus);

        String address = blockChainApi.createWallet(walletName);
        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .walletName(walletName)
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

    private ProgressStatus getCurrentProgress(Report report) {
        List<Chain> chains = findAllByReport(report);
        return chains.get(chains.size()-1).getProgressStatus();
    }

    public List<Chain> findAllByReport(Report report) {
        return chainRepository.findAllByReport(report);
    }
}
