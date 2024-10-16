package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI extends JFrame
{
    //connectedUsersPanel, messagePanel object declarations
    private JPanel connectedUsersPanel, messagePanel;

    //the constructor for the gui window itself, as of now initialized in App.java
    public ClientGUI(String username)
    {
        super("User:" + username);

        //window dimensions
        setSize(1218, 685);
        // the window will be center screen
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //addWindowListener detects window gui events, one of which is window closings
        addWindowListener(new WindowAdapter()
        {
            //addWindowListener will detect window closing effects
            //when a window is attempting to close...
            @Override
            public void windowClosing(WindowEvent e)
            {
                //a dialogue box will pop up that will ask the user "Do you want to exit?"...
                int option = JOptionPane.showConfirmDialog(ClientGUI.this,
                        "Do you want to exit? ", "Exit", JOptionPane.YES_NO_OPTION);

                //there will be two options... yes or no
                //if the yes option chosen...
                if (option == JOptionPane.YES_OPTION)
                {
                    //the gui window will close and the application will be killed
                    ClientGUI.this.dispose();
                }
            }
        });

        //set base background color for gui window
        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        //addGuiComponents will add different gui sections to the window
        //addGuiComponents will set off a small domino effect per se...
        //addConnectedUsersComponents and addChatComponents will invoke
        //so when a ClientGUI object is instantiated, addGuiComponents will trigger...
        //as it is a part of the ClientGUI constructor
        addGuiComponents();
    }
    //invoking addGuiComponents will invoke two different methods...
    private void addGuiComponents()
    {
        addConnectedUsersComponents();
        addChatComponents();
    }



    private void addConnectedUsersComponents()
    {
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users:");
        connectedUsersLabel.setBorder(Utilities.addPadding(10,10,10,10));
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);
        add(connectedUsersPanel, BorderLayout.WEST);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void addChatComponents()
    {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setBorder(Utilities.addPadding(10,10,10,10));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);
        chatPanel.add(messagePanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10,10,10,10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    String input = inputField.getText();
                    if (input.isEmpty() || input.isBlank()) return;
                    inputField.setText("");
                    messagePanel.add(createChatMessageComponent(new Message("Alex", input)));
                    repaint();
                    revalidate();
                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
        inputPanel.add(inputField, BorderLayout.CENTER);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message)
    {
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setBorder(Utilities.addPadding(20,20,10,20));

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel(message.getMessage());
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        return chatMessage;
    }
}
