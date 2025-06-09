package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class TechLogLoadResponse {
    private String title;
    private String nickname;
    private String profileImage;
    private String thumbnailImageUrl;
    private boolean isDeleted;
    private boolean isPublic;
    private LocalDateTime createdAt;

    public static TechLogLoadResponse from(TechLog techLog, User user) {
        return TechLogLoadResponse.builder()
                .title(techLog.getTitle())
                .nickname(user.getNickname())
                .profileImage(user.getProfile().getProfileImage())
                .thumbnailImageUrl(techLog.getThumbnailImageUrl())
                .isDeleted(techLog.isDeleted())
                .isPublic(techLog.isPublic())
                .createdAt(techLog.getCreatedAt())
                .build();

    }





}
