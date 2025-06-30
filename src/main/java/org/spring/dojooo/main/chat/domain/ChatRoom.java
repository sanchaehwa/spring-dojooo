package org.spring.dojooo.main.chat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//MySQL 저장 - 채팅방
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //채팅방 Id

    private String roomName; //채팅방 이름

    private LocalDateTime createdAt; //채팅방 생성 시간

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL)
    private List<ChatParticipant> participants = new ArrayList<>();
}
