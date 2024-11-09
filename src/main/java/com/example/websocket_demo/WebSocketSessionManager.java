package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WebSocketSessionManager
{
    //within-memory list of active users in the websocket session
    private final ArrayList<String> activeUsernames = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    //@Aytowired annotation gives the SimpMessagingTemplate utility to the
    //WebSocketSessionManager object immediately upon object creation.*/
    //You can think of the SimpMessagingTemplate utility as a megaphone that the
    //SimpMessagingTemplate object will use to shout messages to endpoints */
    @Autowired
    public WebSocketSessionManager(SimpMessagingTemplate messagingTemplate)
    {
        this.messagingTemplate = messagingTemplate;
    }

    //adds user to activeUsernames memory list
    public void addUsername(String username)
    {
        activeUsernames.add(username);
    }
    //removes user from activeUsernames memory list
    public void removeUsername(String username)
    {
        activeUsernames.remove(username);
    }
    //broadcast activeUsernames memory list to endpoints subscribed to /topic/active
    public void broadcastActiveUsernames()
    {
        messagingTemplate.convertAndSend("/topic/users", activeUsernames);
        System.out.println("Broadcasting active users to /topic/users " + activeUsernames);
    }
}
