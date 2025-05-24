package org.spring.dojooo.main.users.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.User;

@Getter
@Builder
public class ProfileDetails {
    private Long id;
    private String profileImage;
    private String nickname;
    private String introduction;
    private Boolean isMine;

    public static ProfileDetails of(User user, Boolean isMine) {
        return ProfileDetails.builder()
                .id(user.getId())
                .profileImage(user.getProfile().getProfileImage())
                .nickname(user.getNickname())
                .introduction(user.getProfile().getIntroduction())
                .isMine(isMine)
                .build();
    }
}
