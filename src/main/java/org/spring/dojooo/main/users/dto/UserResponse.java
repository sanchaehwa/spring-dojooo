package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.main.users.domain.User;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String nickname;
    private String email;

    // 정적 팩토리 메서드
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
