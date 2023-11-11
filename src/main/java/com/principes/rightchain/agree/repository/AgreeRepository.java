package com.principes.rightchain.agree.repository;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreeRepository extends JpaRepository<Agree, Long> {
    void deleteByAccountAndReport(Account account, Report report);
}
