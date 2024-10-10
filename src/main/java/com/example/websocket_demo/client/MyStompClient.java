package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyStompClient
{
    //holds session between endpoint and Websocket server
    private StompSession session;
    //holds users name-tag
    private String username;

    //when MyStompClient object is created...
    public MyStompClient(String username) throws ExecutionException, InterruptedException {
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

        //sessionHandler manages communication like transport errors, frame handling, subscriptions, and payload filtering
        StompSessionHandler sessionHandler = new MyStompSessionHandler(username);
        //WebSocket server running locally on port 8080
        //distinction between 8080 and 80...
        //80 is a privileged port and reserved for production
        //whereas 80 is used for typical http communication...
        //8080 is used for server development
        //8080 has fewer restrictions, more convenient for development
        String url = "ws://localhost:8080/ws";

        //store the connection to the websocket server in session
        session = stompClient.connectAsync(url, sessionHandler).get();
    }

    //when a MyStompClient object invokes SendMessage
    public void SendMessage(Message message)
    {
        try
        {
            //the message will be sent to backend controller methods first
            session.send("/app/message", message);
            //logs that the message has been sent
            System.out.println("Message sent: " + message.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void disconnectUser(String username)
    {
        session.send("/app/disconnect", username);
        System.out.println("Disconnected user: " + username);
    }

}

