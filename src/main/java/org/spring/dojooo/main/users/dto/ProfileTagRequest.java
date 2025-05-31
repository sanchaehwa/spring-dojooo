package org.spring.dojooo.main.users.dto;

import lombok.*;
import org.spring.dojooo.global.domain.HasTagName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ProfileTagRequest implements HasTagName {

    //태그이름
    private String tagName;

    //태그 색상 설정
    private String colorcode;

    @Override
    public String getTagName() {
        return tagName;
    }

}
