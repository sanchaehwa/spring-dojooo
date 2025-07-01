package org.spring.dojooo.main.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.main.chat.model.ChatRoomType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//MySQL 저장 - 채팅방
@Entity
@Getter
@NoArgsConstructor
@Table

public class ChatRoom {

    private static final int MAX_ONE_TO_ONE =2;
    private static final int DEFAULT_MAX_GROUP = 50;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //채팅방 Id

    @Column(nullable = false)
    private String roomName; //채팅방 이름

    @Column
    private LocalDateTime createdAt; //채팅방 생성 시간

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL)
    private List<ChatParticipant> participants = new ArrayList<>();

    @Column(nullable = false)
    private Integer maxParticipate;//최대인원

    @Column(nullable = false)
    private Integer nowParticipate; //현재 인원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType chatRoomType;

    @Column(columnDefinition = "TINYINT default 0")
    private Boolean isDeleted;

    //생성시간 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public ChatRoom(String roomName, Integer maxParticipate, Integer nowParticipate,ChatRoomType chatRoomType, Boolean isDeleted) {
        this.roomName = roomName;
        this.chatRoomType = chatRoomType;
        if (chatRoomType == ChatRoomType.ONE_TO_ONE){
            this.maxParticipate = MAX_ONE_TO_ONE;
        } else {
            this.maxParticipate = maxParticipate != null ? maxParticipate : DEFAULT_MAX_GROUP;
        }
        this.nowParticipate = nowParticipate != null ? nowParticipate : 0;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    //참여자추가되면 현재 채팅방 인원도 증가
    public void addParticipant(ChatParticipant chatParticipant) {
        if (!participants.contains(chatParticipant)) {
            this.participants.add(chatParticipant);
            nowParticipate++;
        }
    }
    //톡방 나가면 현재 채팅방 인원 감소
    public void removeParticipant(ChatParticipant chatParticipant) {
        this.participants.remove(chatParticipant);
        nowParticipate--;
    }
    //채팅방 삭제
    public void deleteChatRoom() {
        this.isDeleted = true;
    }

}
