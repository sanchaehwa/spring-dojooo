package org.spring.dojooo.main.follow.dto;

import lombok.*;
import org.spring.dojooo.main.users.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowResponse {
    private String nickname;

    public static FollowResponse from(User user){
        return FollowResponse.builder()
                .nickname(user.getNickname())
                .build();
    }
}
