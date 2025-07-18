package org.spring.dojooo.main.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.chat.model.MessageType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//MongoDB에 저장할 도메인 - 일관성 유지를 위해 Document 이름 설정
@Document(collection = "chat_message")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor

public class ChatMessage {
    @Id
    private String id; //MongoDB objectId
    private Long  senderId;
    private Long roomId;
    private String senderName;
    private String message;
    private LocalDateTime sendAt;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @PrePersist
    protected void onCreate() {
        this.sendAt = LocalDateTime.now();
    }

    @Builder
    public ChatMessage(Long senderId, Long roomId, String senderName, String message, MessageType messageType) {
        this.senderId = senderId;
        this.roomId = roomId;
        this.senderName = senderName;
        this.message = message;
        this.messageType = messageType != null ? messageType : MessageType.TEXT;
    }


}
