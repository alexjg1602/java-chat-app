package com.example.websocket_demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer
{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        //creates an in memory broker with /topic destination prefix
        //any messages with /topic prefixes are routed to this memory broker...
        config.enableSimpleBroker("/topic");
        //messages from endpoints with /app prefixes will be routed to backend controller methods
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        //Websocket door-knock
        //withSockJS for redundancy
        registry.addEndpoint("/ws").withSockJS();
    }
}


