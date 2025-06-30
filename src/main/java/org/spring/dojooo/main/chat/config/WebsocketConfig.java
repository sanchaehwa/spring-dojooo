package org.spring.dojooo.main.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

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
        registry.setMessageSizeLimit(160 * 64 * 1024);
        registry.setSendTimeLimit(100 * 1000);
        registry.setSendBufferSizeLimit(3 * 512 * 1024);
    }
}
