package org.spring.dojooo.main.contents.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


//공통 응답  DTO
@Getter
@Builder
public abstract class MemoResponse {

    private String nickname;
    private String category;
    private List<String> tags;
    private LocalDateTime createdAt;

}
