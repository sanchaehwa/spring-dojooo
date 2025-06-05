package org.spring.dojooo.main.contents.dto;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TechLogTempRequest {
    private String tempId; // = tempUuid
    private String title;
    private String content;
    private String contentImageUrl;
}
