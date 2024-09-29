package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter
{
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders)
    {
        session.subscribe("/topic/messages", new StompFrameHandler()
        {
            @Override
            public Type getPayloadType(StompHeaders headers)
            {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload)
            {
                try
                {
                    if (payload instanceof Message)
                    {
                        Message message = (Message) payload;
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    }
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
    public void handleTransportError(StompSession session, Throwable exception)
    {
        exception.printStackTrace();
    }


}
