package org.spring.dojooo.main.chat.dto;

import lombok.*;
import org.spring.dojooo.main.chat.model.MessageType;
import org.spring.dojooo.main.contents.domain.CheckList;
import org.spring.dojooo.main.contents.dto.CheckListResponse;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SendMessageForm {
    private final Long senderId;
    private final Long messageId;
    private final Long roomId;
    private final String sender;
    private final String message;
    private final LocalDate sendAt;
    private final MessageType messageType;

    public static SendMessageForm from(Long senderId, Long messageId, Long roomId, String sender, String message, LocalDate sendAt,MessageType messageType) {
        return SendMessageForm.builder()
                .senderId(senderId)
                .messageId(messageId)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .sendAt(sendAt)
                .messageType(messageType != null ? messageType : MessageType.TEXT)
                .build();

    }


}
