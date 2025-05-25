package org.spring.dojooo.main.users.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;

import java.util.Optional;

@Getter
@Builder
public class ProfileDetails {
    private Long id;
    private String profileImage;
    private String nickname;
    private String introduction;
    private Boolean isMine;

    // ProfileDetails.java
    public static ProfileDetails of(User user, boolean isOwner) {
        Profile profile = user.getProfile();
        String profileImage = Optional.ofNullable(profile)
                .map(Profile::getProfileImage)
                .orElse("");

        String introduction = Optional.ofNullable(profile)
                .map(Profile::getIntroduction)
                .orElse("");

        return ProfileDetails.builder()
                .id(user.getId())
                .profileImage(profileImage)
                .nickname(user.getNickname())
                .introduction(introduction)
                .isMine(isOwner)
                .build();
    }
    }

