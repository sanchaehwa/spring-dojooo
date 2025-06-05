package org.spring.dojooo.main.contents.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechLogTempResponse {
    //임시저장 요청 DTO
    private String techLogId; //새글이면 Null
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime savedAt;

}
