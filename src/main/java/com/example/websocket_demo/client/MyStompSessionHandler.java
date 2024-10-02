package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter
{
    private String username;

    public MyStompSessionHandler(String username)
    {
        this.username = username;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        System.out.println("connected");
        System.out.println("subscribed to /topic/messages");
        //once session is established, sub to /topic/messages
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
    }
    @Override
    //will invoke if session unexpectedly folds
    public void handleTransportError(StompSession session, Throwable exception)
    {
        exception.printStackTrace();
    }


}
