package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;

import java.util.List;
//사용자에 등록되어있는 전체 테그를 조회하는 DTO
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDetailsList {
    private Long userId;
    private List<TagDetails> tags;

    public static TagDetailsList from(User user, List<ProfileTag> profileTags) {
        List<TagDetails> tagDetailsList = profileTags.stream()
                .map(profileTag -> TagDetails.from(user, profileTag))
                .toList();
        return TagDetailsList.builder()
                .userId(user.getId())
                .tags(tagDetailsList).build();
    }



}
