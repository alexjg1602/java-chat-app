package com.example.websocket_demo.client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI extends JFrame
{
    public ClientGUI(String username)
    {
        super("User:" + username);

        setSize(1218, 685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this,
                        "Do you want to exit? ", "Exit", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION)
                {
                    ClientGUI.this.dispose();
                }
            }
        });

    }
}
