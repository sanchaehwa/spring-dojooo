package org.spring.dojooo.main.users.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    //자기소개
    private String introduction;

    private String profileImageUrl;

    private List<String> tagNames;

}
