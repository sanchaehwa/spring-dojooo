package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.model.TodoState;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    List<CheckList> findByUserAndEndDateBetweenAndIsDeletedFalse(User user, LocalDate startDate, LocalDate endDate);

    //유저가 특정 날짜에 등록한 할일 목록을 모두 가져오는
    @Query("SELECT c FROM CheckList c WHERE c.user.id = :userId AND c.startDate = :date AND c.isDeleted = false")
    List<CheckList> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

}
