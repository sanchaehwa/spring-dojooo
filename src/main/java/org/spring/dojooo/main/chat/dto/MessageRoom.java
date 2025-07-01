package org.spring.dojooo.main.chat.dto;

import lombok.*;
import org.spring.dojooo.main.chat.model.ChatRoomType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MessageRoom {
    private Long roomId;
    private String roomName;
    private ChatRoomType roomType; //개인톡방 OR 단톡방
    private Integer nowParticipate; //현재 참여 인원
    private Integer maxParticipate; //최대 인원
    private Boolean isDeleted;

}
