package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechLogEditRequest {
    private String title;
    private String content;
    private String thumbnailImageUrl;
    private String contentImageUrl;
    private boolean isPublic;
    private LocalDateTime createdAt;
}
