package org.spring.dojooo.main.contents.repository;

import org.spring.dojooo.main.contents.domain.ChecklistTag;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistTagRepository extends JpaRepository<ChecklistTag, Long> {
    List<ChecklistTag> findAllByUser(User user);
    Optional<ChecklistTag> findByUserAndTag_TagNameAndIsDeletedFalse(User user, String tagName);

}
