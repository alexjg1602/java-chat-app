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
        //send /app/connect" and username to broker then the broker will see /app and send to backend controller...
        //then when the websocket controller sees /connect, connectUser will invoke
        session.send("/app/connect", username);
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
        System.out.println("subscribed to /topic/messages");

    }
    @Override
    //will invoke if session unexpectedly folds
    public void handleTransportError(StompSession session, Throwable exception)
    {
        exception.printStackTrace();
    }

}
