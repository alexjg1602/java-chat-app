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
    //config is a preconfigured instance of MessageBrokerRegistry that MessageBrokerRegistry...
    //injects into configureMessageBroker upon invocation so that config can use configureMessageBroker.
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        //the config object will create a simple(hence simp) in memory serverside message broker...
        //that will be responsible for managing message subscriptions and broadcasting messages to those subscribers.
        //"/topic" is a channel prefix that clients can sub to
        //"/topic/news" or "/topic/sports" for example
        config.enableSimpleBroker("/topic");
        //before the broker can broadcast messages to subscribed clients...
        //the message that clients want to be sent needs to be handled by backend controller methods
        //messages will first go to /app where the backend logic operates
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        //broker entry point for clients
        registry.addEndpoint("/ws").withSockJS();
    }
}


