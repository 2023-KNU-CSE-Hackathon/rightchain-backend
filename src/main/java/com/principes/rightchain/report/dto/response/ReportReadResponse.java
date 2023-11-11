package com.principes.rightchain.report.dto.response;

import com.principes.rightchain.report.entity.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportReadResponse {
    private String caseNum;
    private String title;
    private String content;
    private Boolean isCaseClose;

    public ReportReadResponse(Report report) {
        this.caseNum = "2023" + String.format("%04d", report.getId());
        this.title = report.getTitle();
        this.content = report.getContent();
        this.isCaseClose = report.getIsCaseClose();
    }
}
