package org.spring.dojooo.main.chat.config;

import lombok.RequiredArgsConstructor;
import org.spring.dojooo.main.chat.dto.SendMessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate; //서버에서 클


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
   // /ws : (client) websocket 연결 Endpoint, * CORS 설정, 모든 Origin 접근 가능
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); //클라이언트가 메시지 받는
        registry.setApplicationDestinationPrefixes("/pub"); //클라이언트가 메시지를 보낼때 사용
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry){
        registry.setMessageSizeLimit(160 * 64 * 1024); //메시지 크기 제한
        registry.setSendTimeLimit(100 * 1000); //메시지 전송 제한 시간
        registry.setSendBufferSizeLimit(3 * 512 * 1024); //버퍼 사이즈 제한
    }
    private void broadcastMessage(String topic, SendMessageForm message){
        messagingTemplate.convertAndSend(topic, message);
    }
}
