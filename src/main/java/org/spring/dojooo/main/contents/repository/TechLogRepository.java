package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechLogRepository extends JpaRepository<TechLog, Long> {

    // 삭제되지 않은 글만 ID로 조회
    Optional<TechLog> findByTechLogidAndIsDeletedFalse(Long techLogid);

    // 삭제되지 않은 동일 제목 글 조회
    Optional<TechLog> findByTitleAndIsDeletedFalse(String title);

    // 특정 유저가 작성한 특정 글 조회 (삭제되지 않은 것만)
    Optional<TechLog> findByTechLogidAndUserAndIsDeletedFalse(Long techLogid, User user);

    // 특정 유저의 모든 글 (공개/비공개 상관없이 삭제되지 않은 글만)
    @Query("SELECT t FROM TechLog t WHERE t.user.id = :userId AND t.isDeleted = false")
    List<TechLog> findAllByUserId(@Param("userId") Long userId);

    // 모든 공개 글 (삭제되지 않은 것만) + 유저/프로필 fetch join
    @Query("SELECT t FROM TechLog t JOIN FETCH t.user u JOIN FETCH u.profile WHERE t.isPublic = true AND t.isDeleted = false")
    List<TechLog> findAllByIsPublic();
}
