package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechLogRepository extends JpaRepository<TechLog, Long> {
    Optional<TechLog> findByTechLogid(Long techLogid);
    Optional<TechLog> findBytitle(String title);
    Optional<TechLog> findByTechLogidAndUser(Long techLogid, User user);}
