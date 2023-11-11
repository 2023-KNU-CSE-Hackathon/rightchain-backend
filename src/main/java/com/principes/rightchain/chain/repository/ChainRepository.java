package com.principes.rightchain.chain.repository;

import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.chain.entity.ProgressStatus;
import com.principes.rightchain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChainRepository extends JpaRepository<Chain, Long> {
    @Query("SELECT e.progressStatus FROM Chain e WHERE e.report.id = :caseId ORDER BY e.id DESC")
    ProgressStatus findProgressStatusByCaseId(@Param("caseId") Long caseId);

    List<Chain> findAllByReport(Report report);
}
