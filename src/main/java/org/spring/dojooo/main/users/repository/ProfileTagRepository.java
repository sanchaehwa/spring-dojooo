package org.spring.dojooo.main.users.repository;

import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {
    List<ProfileTag> findAllByUser(User user);

    Optional<ProfileTag> findByUserAndTagNameAndIsDeletedFalse(User user, String tagName); //삭제되지않는 테

}
