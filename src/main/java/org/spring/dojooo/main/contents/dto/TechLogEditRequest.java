package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechLogEditRequest {
    private Long id;
    private String title;
    private String content;
    private boolean idRead;
    private LocalDateTime createdAt;
}
