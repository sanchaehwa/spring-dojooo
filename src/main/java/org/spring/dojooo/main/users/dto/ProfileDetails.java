package org.spring.dojooo.main.users.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.dojooo.main.contents.domain.Memo.Tag;
import org.spring.dojooo.main.users.domain.Profile;
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

        List<String> tagNames = Optional.ofNullable(user.getTags())
                .orElse(List.of()) //null이면 빈 리스트 처리
                .stream()
                .filter(Tag::isShowOnProfile) //프로필에 보여질 테그만
                .map(Tag::getTagName) //Tag 객체가 아니라, Tag의 이름만 추출
                .toList();

        return ProfileDetails.builder()
                .id(user.getId())
                .profileImage(profileImage)
                .nickname(user.getNickname())
                .introduction(introduction)
                .isMine(isOwner)
                .tags(tagNames)
                .build();
    }
    }

