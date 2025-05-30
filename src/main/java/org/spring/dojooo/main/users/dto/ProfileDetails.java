package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class ProfileDetails {
    private Long id;
    private String profileImage;
    private String nickname;
    private String introduction;
    private Boolean isMine;
    private static final String DEFAULT_PROFILE_IMAGE = "https://dojooo.s3.ap-northeast-2.amazonaws.com/profile/80aefad7-3_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF.jpg";
    private List<String> tags;

    public static ProfileDetails of(User user, boolean isOwner) {
        Profile profile = user.getProfile();
        String profileImage = (profile != null && profile.getProfileImage() != null && !profile.getProfileImage().isBlank())
                ? profile.getProfileImage()
                : DEFAULT_PROFILE_IMAGE;;

        String introduction = Optional.ofNullable(profile)
                .map(Profile::getIntroduction)
                .orElse("");

        return ProfileDetails.builder()
                .id(user.getId())
                .profileImage(profileImage)
                .nickname(user.getNickname())
                .introduction(introduction)
                .tags(user.getVisibleProfileTagNames())
                .isMine(isOwner)
                .build();
    }
    }

