package org.spring.dojooo.main.users.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProfileSaveRequest {
    private String introduction;
    private String profileImageUrl;

}
