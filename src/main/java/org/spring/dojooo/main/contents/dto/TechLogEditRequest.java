package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechLogEditRequest {
    private Long techLogId;
    private String title;
    private String content;
    private boolean isPublic;
    private LocalDateTime createdAt;
}
