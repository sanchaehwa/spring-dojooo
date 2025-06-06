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
    private String contentImageUrl; //본문에 들어갈 사진만 임시저장
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
                .contentImageUrl(this.contentImageUrl)
                .savedAt(LocalDateTime.now())
                .build();
    }
}
