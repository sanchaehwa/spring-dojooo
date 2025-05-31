package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.CheckListTag;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistTagRepository extends JpaRepository<CheckListTag, Long> {
    List<CheckListTag> findAllByUser(User user);
    Optional<CheckListTag> findByUserAndTagNameAndIsDeletedFalse(User user, String tagName);
}
