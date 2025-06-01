package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProfileTagUpdateRequest implements HasTagName {

    private String originalTagName;
    private String newTagName;

    private String originalColorCode;
    private String newColorCode;

    @Override
    public String getTagName() {
        return newTagName != null && !newTagName.isBlank() ? newTagName : originalTagName;
    }


}
