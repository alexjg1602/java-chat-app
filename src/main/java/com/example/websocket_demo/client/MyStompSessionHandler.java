package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyStompSessionHandler extends StompSessionHandlerAdapter
{
    private String username;
    private MessageListener messageListener;

    public MyStompSessionHandler(MessageListener messageListener,String username)
    {
        this.username = username;
        this.messageListener = messageListener;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        System.out.println("connected");
        //send /app/connect" and username to broker then the broker will see /app and send to backend controller...
        //then when the websocket controller sees /connect, connectUser will invoke
        //once session is established, sub to /topic/messages and do the following...
        session.subscribe("/topic/messages", new StompFrameHandler()
        {
            @Override
            //what kind of payload should endpoints expect from incoming messages...POJO from Message class
            public Type getPayloadType(StompHeaders headers)
            {
                return Message.class;
            }

            @Override
            //process incoming messages
            public void handleFrame(StompHeaders headers, Object payload)
            {
                try
                {
                    //checks whether the payload is an instance of Message class
                    if (payload instanceof Message)
                    {
                        //extract general payload object and cast it into message so it can be worked with
                        Message message = (Message) payload;
                        //onMessageRecieve from clientgui will invoke when a message is received
                        messageListener.onMessageRecieve(message);
                        //print message contents
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    }
                    //payload isn't a Message object
                    else
                    {
                        System.out.println("Received unknown payload: " + payload.getClass());
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("subscribed to /topic/messages");

        //when subbed to /topic/users, which will give active users list to the stomp-client and websocket-server...
        //do the following...
        session.subscribe("/topic/users", new StompFrameHandler()
        {
            @Override
            public Type getPayloadType(StompHeaders headers)
            {
                //give the active users list class type from /topic/users back to the caller for it to be handled
                return new ArrayList<String>().getClass();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload)
            {
                try
                {
                    //if the payload from /topic/users is an arraylist object...
                    if (payload instanceof ArrayList)
                    {
                        //put the arraylist in an object called activeUsers
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        //onActiveUsersUpdated declared in message listener and defined in clientgui will invoke
                        messageListener.onActiveUsersUpdated(activeUsers);
                        System.out.println("Active users: " + activeUsers);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("subscribed to /topic/users");

        //send /app/connect" and username to broker then the broker will see /app and send to backend controller...
        //then when the websocket controller sees /connect, connectUser will invoke
        session.send("/app/connect", username);
        session.send("/app/request-users", "");

    }
    @Override
    //will invoke if session unexpectedly folds
    public void handleTransportError(StompSession session, Throwable exception)
    {
        exception.printStackTrace();
    }

}
