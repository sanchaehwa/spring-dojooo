package org.spring.dojooo.main.contents.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TechLogTempRedisDTO {
    private String tempId;
    private String techLogId; // String 타입
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime savedAt;
    public void updateSavedAt(LocalDateTime now) {
        this.savedAt = now;
    }
    public TechLogTempRedisDTO withUpdatedSavedAt() {
        return TechLogTempRedisDTO.builder()
                .tempId(this.tempId)
                .techLogId(this.techLogId)
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .savedAt(LocalDateTime.now())
                .build();
    }
}
