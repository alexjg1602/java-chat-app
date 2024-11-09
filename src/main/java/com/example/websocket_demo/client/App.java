package com.example.websocket_demo.client;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class App
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //app sign in
                String username = JOptionPane.showInputDialog(null,
                        "Enter username:",
                        "Chat App",
                        JOptionPane.QUESTION_MESSAGE);

                if (username == null || username.isEmpty() ||username.length() < 1)
                {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid username",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }


                ClientGUI clientGUI = null;
                try {
                    clientGUI = new ClientGUI(username);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientGUI.setVisible(true);

            }
        });
    }
}
