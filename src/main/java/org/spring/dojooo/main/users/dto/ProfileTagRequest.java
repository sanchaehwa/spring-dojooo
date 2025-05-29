package org.spring.dojooo.main.users.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ProfileTagRequest {

    //태그이름
    private String tagName;

    //태그 색상 설정
    private String colorcode;

}
