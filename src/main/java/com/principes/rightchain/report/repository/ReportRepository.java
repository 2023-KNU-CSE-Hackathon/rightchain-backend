package com.principes.rightchain.report.repository;

import com.principes.rightchain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
