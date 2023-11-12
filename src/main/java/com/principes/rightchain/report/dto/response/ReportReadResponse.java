package com.principes.rightchain.report.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.report.entity.Report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportReadResponse {
    private String name;
    private String caseNum;
    private String title;
    private String content;
    private Boolean isCaseClose;
    private String schoolName;
    private List<ReportReadInChainDto> chains;
    private List<String> agreeNames;

    public ReportReadResponse(Report report, List<Agree> agrees, List<Chain> chains) {
        this.name = report.getAccount().getName();
        this.caseNum = "2023" + String.format("%04d", report.getId());
        this.title = report.getTitle();
        this.content = report.getContent();
        this.isCaseClose = report.getIsCaseClose();
        this.schoolName = report.getSchoolName();
        this.chains = chains.stream()
                .map(chain -> ReportReadInChainDto.builder()
                        .walletName(chain.getWalletName())
                        .progressStatus(chain.getProgressStatus())
                        .address(chain.getAddress())
                        .build())
                .collect(Collectors.toList());
        this.agreeNames = agrees.stream()
                .map(agree -> agree.getAccount().getName())
                .collect(Collectors.toList());
    }
}
