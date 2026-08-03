package com.damianos.fleet.vehicletracking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // backend -> frontend
        config.enableSimpleBroker("/topic");

        // frontend -> backend
        config.setApplicationDestinationPrefixes("/app");

    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        // bez SockJS - zostawiamy tak jak było

    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

        registry
                .setSendBufferSizeLimit(2 * 1024 * 1024)  // 1 MB
                .setSendTimeLimit(20000);             // 20 sekund

    }

}