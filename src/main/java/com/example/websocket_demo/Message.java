package com.example.websocket_demo;

public class Message
{
    //user sending message
    private String user;
    //message content
    private String message;

    //no arg constructor
    public Message(){}
    //message object constructor
    public Message(String user, String message)
    {
        //Message object input values
        this.user = user;
        this.message = message;
    }

    //return object input values to caller
    public String getUser()
    {
        return user;
    }
    public String getMessage()
    {
        return message;
    }
}

