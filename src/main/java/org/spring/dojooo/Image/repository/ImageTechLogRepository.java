package org.spring.dojooo.Image.repository;

import org.spring.dojooo.Image.domain.TechLogImage;
import org.spring.dojooo.Image.model.TechLogImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ImageTechLogRepository extends JpaRepository<TechLogImage, Long> {
    Optional<TechLogImage> findByTechLog_TechLogidAndImageType(Long techLogId, TechLogImageType imageType);
}
