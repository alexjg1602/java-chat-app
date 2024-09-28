package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController
{
    private final SimpMessagingTemplate messagingTemplate;

    //@Aytowired annotation gives the SimpMessagingTemplate utility to the
    //WebsocketController object immediately upon object creation.*/
    //You can think of the SimpMessagingTemplate utility as a megaphone that the
    //WebsocketController object will use to shout messages to endpoints */
    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate)
    {
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
}

