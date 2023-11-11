package com.principes.rightchain.chain.entity;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.report.entity.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Long id;

    private ProgressStatus progressStatus;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "report_id")
    private Report report; // 사건 번호

    @Builder
    public Chain(Long id, ProgressStatus progressStatus, String address, Report report) {
        this.id = id;
        this.progressStatus = progressStatus;
        this.address = address;
        this.report = report;
    }
}
