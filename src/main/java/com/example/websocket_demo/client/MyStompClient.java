package com.example.websocket_demo.client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

public class MyStompClient
{
    //holds session between endpoint and Websocket server
    private StompSession session;
    //holds users name-tag
    private String username;

    //when MyStompClient it will take in a username
    public MyStompClient(String username)
    {
        this.username = username;

        //message travel path(s) will be stored in an ArrayList
        //you can think of transports as a route table
        List<Transport> transports = new ArrayList<>();
        //first travel path will be between endpoint(s) and websocket server
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        //sockJsClient for redundancy
        SockJsClient sockJsClient = new SockJsClient(transports);
        //first try websockets, if connecting via an older browser or network config,
        // and native websocket support is lacking, sockJsClient will suit*/
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        //convert outgoing and incoming messages (serialization and deserialization) to json
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    }
}

