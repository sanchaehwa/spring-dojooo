package org.spring.dojooo.main.contents.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class TechLogAddRequest {
    private Long userId;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createDate;
    @NotNull(message = "공개여부를 설정해주세요")
    private boolean isRead;

    public TechLog toEntity(User user) {
        return TechLog.builder()
                .user(user)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .isRead(isRead)
                .build();

    }


}
