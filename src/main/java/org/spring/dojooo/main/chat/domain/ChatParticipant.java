package org.spring.dojooo.main.chat.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.main.follow.domain.Follow;
import org.spring.dojooo.main.users.domain.User;

@Entity
@Getter
@NoArgsConstructor
@Table
public class ChatParticipant { //현재 채팅방에 참여중인 사용자

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TINYINT default 0")
    private boolean hasExited; // 나간 사용자 체크

    @Column(nullable=false)
    private boolean isInvited;

    @Column(nullable=false)
    private boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_id")
    private Follow follow; //팔로우 관계에 있어야지 1:1 채팅 가능하게 하려고

    @Builder
    public ChatParticipant(ChatRoom chatRoom, User user, Follow follow, boolean hasExited, boolean isInvited, boolean isAccepted) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.isInvited = isInvited;
        this.isAccepted = isAccepted;
        this.hasExited = hasExited;
        this.follow = follow;
    }
    public void acceptInvitation() { //A가 B 사용자한테 요청을 보냈을때 요청을 수락한경우
        this.isAccepted = true;
        this.hasExited = false;
    }
    //채팅방 나감
    public void exitRoom(){
        this.hasExited = true;
    }
    //채팅방 재입장
    public void rejoinRoom(){
        this.hasExited = false;
    }

}
