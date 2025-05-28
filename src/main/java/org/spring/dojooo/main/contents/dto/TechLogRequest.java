package org.spring.dojooo.main.contents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;
import java.util.List;

//공통 요청 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class TechLogRequest {

    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private boolean isRead;
    private LocalDateTime createdAt;

    TechLog toEntity(User user) {
        return TechLog.builder()
                .user(user)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .isRead(isRead)
                .build();
    }



}
