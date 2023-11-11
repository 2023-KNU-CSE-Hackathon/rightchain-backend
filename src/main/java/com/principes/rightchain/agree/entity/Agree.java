package com.principes.rightchain.agree.entity;

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
public class Agree {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agree_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Report.class)
    @JoinColumn(name = "report_id")
    private Report report;

    @Builder
    public Agree(Long id, Account account, Report report) {
        this.id = id;
        this.account = account;
        this.report = report;
    }
}
