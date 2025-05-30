package org.spring.dojooo.main.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSaveRequest {
    private String introduction;
    private String profileImageUrl;

}
