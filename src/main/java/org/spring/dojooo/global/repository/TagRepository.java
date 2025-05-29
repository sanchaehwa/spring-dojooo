package org.spring.dojooo.global.repository;

import org.spring.dojooo.global.domain.Tag;
import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagNameAndUser(String tagName, User user);

}
