package org.spring.dojooo.main.users.dto;


import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProfileUpdateRequest {

    //자기소개
    private String introduction;

    private String profileImageUrl;

    private List<String> tagNames;

}
