package com.principes.rightchain.report.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.report.entity.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportCreateRequest {
    private String title;
    private String content;

    public Report toEntity(Account account) {
        return Report.builder()
                .title(this.title)
                .content(this.content)
                .isCaseClose(false)
                .schoolName(account.getSchoolName())
                .account(account).build();
    }
}
