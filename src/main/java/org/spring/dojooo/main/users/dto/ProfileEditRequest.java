package org.spring.dojooo.main.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.users.domain.Profile;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEditRequest {

    //자기소개
    private String introduction;
    //프로필 이미지
    private MultipartFile profileImage; //프로필 이미지 추가

    public Profile toProfile(String imageUrl){
        return Profile.builder()
                .profileImage(imageUrl)
                .introduction(introduction)
                .build();
    }
}
