package org.spring.dojooo.main.chat.dto;

import lombok.*;
import org.spring.dojooo.main.chat.model.MessageType;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.dto.CheckListResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SendMessageForm {
    private String id;
    private final Long senderId;
    private final Long roomId;
    private final String senderName;
    private final String message;
    private final LocalDateTime sendAt;
    private final MessageType messageType;

    public static SendMessageForm from(String id, Long senderId, Long roomId, String senderName, String message, LocalDateTime sendAt, MessageType messageType) {
        return SendMessageForm.builder()
                .id(id)
                .senderId(senderId)
                .roomId(roomId)
                .senderName(senderName)
                .message(message)
                .sendAt(sendAt)
                .messageType(messageType != null ? messageType : MessageType.TEXT)
                .build();
    }
    //현재 시간으로 sendAt을 설정한 새 객체 반환 매서드
    public SendMessageForm setSendDateTocurrentTime() {
        return SendMessageForm.builder()
                .id(this.id)
                .senderId(this.senderId)
                .roomId(this.roomId)
                .senderName(this.senderName)
                .message(this.message)
                .sendAt(LocalDateTime.now())
                .messageType(this.messageType)
                .build();
    }


}
