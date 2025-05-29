package org.spring.dojooo.main.users.repository;

import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {
    List<ProfileTag> findAllByUser(User user);
    Optional<ProfileTag> findByUserAndTag_TagNameAndIsDeletedFalse(User user, String tagName); //삭제되지않는 테

    @Query("SELECT CASE WHEN COUNT(pt) > 0 THEN true ELSE false END FROM ProfileTag pt WHERE pt.user.id = :userId AND pt.tag = :tag")
    boolean existsByUserIdAndTag(@Param("userId") Long userId, @Param("tag") Tag tag);}
