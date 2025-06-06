package org.spring.dojooo.main.contents.dto;

import lombok.*;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;


//공통 응답  DTO
@Getter
@Builder
public class TechLogResponse {

   private String nickname;
   private String title;
   private String content;
   private Long userId;
   private Long techLogId;
   private String profileImage;
   private String contentImageUrl;
   private String thumbnailImageUrl;
   private boolean isDeleted;
   private boolean isPublic;
   private LocalDateTime createdAt;

   public static TechLogResponse from(TechLog techLog, User user) {
      return TechLogResponse.builder()
              .nickname(user.getNickname())
              .userId(user.getId())
              .techLogId(techLog.getTechLogid())
              .profileImage(user.getProfile().getProfileImage()!=null ? user.getProfile().getProfileImage() : null)
              .title(techLog.getTitle()) // 추가
              .content(techLog.getContent()) // 추가
              .contentImageUrl(techLog.getContentImageUrl())
              .thumbnailImageUrl(techLog.getThumbnailImageUrl())
              .isDeleted(techLog.isDeleted())
              .isPublic(techLog.isPublic())
              .createdAt(techLog.getCreatedAt())
              .build();
   }

}
