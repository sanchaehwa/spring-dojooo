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
   private Long userId;
   private String profileImage;
   private String imageUrl;
   private boolean isDeleted;
   private boolean isRead;
   private LocalDateTime createdAt;

   public static TechLogResponse from(TechLog techLog, User user) {
       return TechLogResponse.builder()
               .nickname(user.getNickname())
               .userId(user.getId())
               .profileImage(user.getProfile().getProfileImage()!=null ? user.getProfile().getProfileImage() : null)
               .imageUrl(techLog.getImageUrl())
               .isDeleted(techLog.isDeleted())
               .isRead(techLog.isRead())
               .createdAt(techLog.getCreatedAt())
               .build();
   }

}
