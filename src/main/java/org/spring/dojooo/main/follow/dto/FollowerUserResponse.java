package org.spring.dojooo.main.follow.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.User;

@Getter
@Builder
public class FollowerUserResponse {
    private Long userId;
    private String nickname;
    private String profileImage;
    private boolean isFollower;

    public static FollowerUserResponse from(User user,boolean isFollower) {
        return FollowerUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfile().getProfileImage())
                .isFollower(isFollower)
                .build();

    }

}
