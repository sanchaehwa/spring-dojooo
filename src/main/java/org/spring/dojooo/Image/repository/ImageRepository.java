package org.spring.dojooo.Image.repository;

import org.spring.dojooo.Image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(Long userId);
}
