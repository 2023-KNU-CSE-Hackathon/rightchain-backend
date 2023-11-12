package com.principes.rightchain.agree.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.report.entity.Report;

public interface AgreeRepository extends JpaRepository<Agree, Long> {
    boolean existsByAccountAndReport(Account account, Report report);
    void deleteByAccountAndReport(Account account, Report report);
    List<Agree> findAllByReport(Report report);
}
