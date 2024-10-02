package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController
{
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    //@Aytowired annotation gives the SimpMessagingTemplate utility to the
    //WebsocketController object immediately upon object creation.*/
    //You can think of the SimpMessagingTemplate utility as a megaphone that the
    //WebsocketController object will use to shout messages to endpoints */
    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate, WebSocketSessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
        this.messagingTemplate = messagingTemplate;
    }

    //messages from endpoints addressed to the controller will be handled by...
    //logs that the message was received
    //sends/broadcasts the received message to subscribed endpoints using messagingTemplate object via convertAndSend
    //logs that the message was sent/broadcast(past tense)
    @MessageMapping("/message")
    public void handleMessage(Message message)
    {
        System.out.println("Received message from user: " + message.getUser() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Sent message to /topic/messages: " + message.getUser() + ": " + message.getMessage());
    }

    //@MessageMapping listens for WebSocket messages on /connect and /disconnect, then invokes the appropriate methods
    //when a user joins a session...
    //connectUser will trigger
    @MessageMapping("/connect")
    public void connectUser(String username)
    {
        //sessionManager object from WebSocketSessionManager.java...
        // adds user to activeUsernames from WebSocketSessionManager.java
        sessionManager.addUsername(username);
        //sessionManager object from WebSocketSessionManager.java...
        // blasts updated activeUsernames list from WebSocketSessionManager to endpoints subbed to /topic/active
        sessionManager.broadcastActiveUsernames();
        //log that user has joined session
        System.out.println(username + "connected");
    }

    //when a user leaves a session...
    //disconnectUser will trigger
    @MessageMapping("/disconnect")
    public void disconnectUser(String username)
    {
        //sessionManager object from WebSocketSessionManager.java...
        // removes user from activeUsernames from WebSocketSessionManager.java
        sessionManager.removeUsername(username);
        //sessionManager object from WebSocketSessionManager.java...
        // blasts updated activeUsernames list from WebSocketSessionManager to endpoints subbed to /topic/active
        sessionManager.broadcastActiveUsernames();
        //log that user has departed session
        System.out.println(username + "disconnected");
    }

}

