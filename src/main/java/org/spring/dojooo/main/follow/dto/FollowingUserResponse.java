package org.spring.dojooo.main.follow.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.User;

@Getter
@Builder
public class FollowingUserResponse {
    private Long userId;
    private String nickname;
    private String profileImage;
    private boolean isFollowing;

    public static FollowingUserResponse from(User user, boolean isFollowing){
        return FollowingUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfile().getProfileImage())
                .isFollowing(isFollowing)
                .build();
    }
}
