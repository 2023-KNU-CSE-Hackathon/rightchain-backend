package com.principes.rightchain.chain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.principes.rightchain.report.entity.Report;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Long id;

    private String walletName;
    private ProgressStatus progressStatus;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Report.class)
    @JoinColumn(name = "report_id")
    private Report report; // 사건 번호

    @Builder
    public Chain(Long id, String walletName, ProgressStatus progressStatus, String address, Report report) {
        this.id = id;
        this.walletName = walletName;
        this.progressStatus = progressStatus;
        this.address = address;
        this.report = report;
    }
}
