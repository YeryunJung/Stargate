package com.ssafy.stargate.config;

import com.ssafy.stargate.handler.StompHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").
                setAllowedOrigins("*");
    }

    // 메세지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // prefix 가 붙은 메세지를 발행시 브로커가 구독자가 전달
        registry.setApplicationDestinationPrefixes("/sub");
        // 스프링에서 제공하는 내장 브로커 사용
        // prefix 가 붙은 메세지가 송신 됭
        registry.enableSimpleBroker("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}
