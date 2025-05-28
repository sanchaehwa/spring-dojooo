package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    @Query("SELECT c FROM CheckList c WHERE c.user.id = :userId AND c.schedule = CURRENT_DATE AND c.isDeleted = false")
    List<CheckList> findTodayTasksByUser(@Param("userId") Long userId);
}
