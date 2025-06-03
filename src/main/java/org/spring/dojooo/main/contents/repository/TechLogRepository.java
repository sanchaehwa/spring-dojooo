package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.TechLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechLogRepository extends JpaRepository<TechLog, Long> {
    Optional<TechLog> findByTechLogId(Long techLogId);
    Optional<TechLog> findByTechLogTitle(String title);
}
