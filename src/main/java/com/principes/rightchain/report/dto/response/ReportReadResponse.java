package com.principes.rightchain.report.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.report.entity.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportReadResponse {
    private String caseNum;
    private String title;
    private String content;
    private Boolean isCaseClose;
    private String schoolName;
    private List<ReportReadInChainDto> reportReadInChainDtoList;

    public ReportReadResponse(Report report, List<Chain> chains) {
        this.caseNum = "2023" + String.format("%04d", report.getId());
        this.title = report.getTitle();
        this.content = report.getContent();
        this.isCaseClose = report.getIsCaseClose();
        this.schoolName = report.getSchoolName();
        this.reportReadInChainDtoList = chains.stream()
                .map(chain -> ReportReadInChainDto.builder()
                        .progressStatus(chain.getProgressStatus())
                        .address(chain.getAddress())
                        .build())
                .collect(Collectors.toList());
    }
}
