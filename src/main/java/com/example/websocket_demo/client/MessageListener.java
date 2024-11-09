package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.apache.catalina.User;

import java.util.ArrayList;

public interface MessageListener
{
    void onMessageRecieve(Message message);
    void onActiveUsersUpdated(ArrayList<String> users);

}
