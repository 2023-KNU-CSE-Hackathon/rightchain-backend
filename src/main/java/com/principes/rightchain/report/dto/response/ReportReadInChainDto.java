package com.principes.rightchain.report.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.chain.entity.ProgressStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportReadInChainDto {
    private ProgressStatus progressStatus;
    private String address;

    @Builder
    public ReportReadInChainDto(ProgressStatus progressStatus, String address) {
        this.progressStatus = progressStatus;
        this.address = address;
    }
}
