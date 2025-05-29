package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.main.users.domain.ProfileTag;
import org.spring.dojooo.main.users.domain.User;
//단일 테그
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDetails {
    private Long id;
    private String tagName;
    private String colorCode;
    private boolean isDeleted;
    private boolean showOnProfile;

    public static TagDetails from(User user, ProfileTag profileTag) {
        return TagDetails.builder()
                .id(user.getId())
                .tagName(profileTag.getTag().getTagName())
                .colorCode(profileTag.getColorCode())
                .isDeleted(profileTag.isDeleted())
                .showOnProfile(profileTag.isShowOnProfile())
                .build();
    }
}
