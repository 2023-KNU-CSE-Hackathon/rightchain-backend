package com.principes.rightchain.report.entity;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.chain.entity.Chain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private String title;
    private String content;
    private Boolean isCaseClose;
    private String schoolName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chain> chains = new ArrayList<>();

    @Builder
    public Report(Long id, String title, String content, Boolean isCaseClose, String schoolName, Account account, List<Chain> chains) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isCaseClose = isCaseClose;
        this.schoolName = schoolName;
        this.account = account;
        this.chains = chains;
    }
}
