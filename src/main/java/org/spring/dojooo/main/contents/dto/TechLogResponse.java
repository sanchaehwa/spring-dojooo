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
   private Long techLogId;
   private String profileImage;
   private String imageUrl;
   private boolean isDeleted;
   private boolean isPublic;
   private LocalDateTime createdAt;

   public static TechLogResponse from(TechLog techLog, User user) {
       return TechLogResponse.builder()
               .nickname(user.getNickname())
               .userId(user.getId())
               .techLogId(techLog.getTechLogid())
               .profileImage(user.getProfile().getProfileImage()!=null ? user.getProfile().getProfileImage() : null)
               .imageUrl(techLog.getImageUrl())
               .isDeleted(techLog.isDeleted())
               .isPublic(techLog.isPublic())
               .createdAt(techLog.getCreatedAt())
               .build();
   }

}
