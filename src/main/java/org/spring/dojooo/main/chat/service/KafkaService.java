package org.spring.dojooo.main.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.main.chat.dto.SendMessageForm;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final SimpMessagingTemplate messagingTemplate;

    //채팅 메시지
    public void broadcastMessage(String topic, SendMessageForm message){
        messagingTemplate.convertAndSend(topic, message);
    }
    //채팅방 초대 알림
    public void broadcastInvitationAlert(Long toUserId, String roomName, Long roomId){
        String topic = "/sub/alarm/" + toUserId + "/" + roomName;
        messagingTemplate.convertAndSend(topic, roomId);
    }
    //메시지 알림
    public void broadcastAlert(Long userId, String message){
        messagingTemplate.convertAndSend("/sub/user/" + userId, message);

    }


}
