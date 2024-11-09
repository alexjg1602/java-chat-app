package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener
{
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane messagePanelScrollPane;

    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("User:" + username);
        this.username = username;
        //whenever the ClientGUI is created, a parallel StompClient object will be created on the client in running memory...
        //the StompClient will handle the backend logic of communicating with the websocket server
        myStompClient = new MyStompClient(this, username);

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
                    //the StompClient will invoke its disconnectUser method and the websocket server controller will react
                    myStompClient.disconnectUser(username);
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
        connectedUsersPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users:");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);
        add(connectedUsersPanel, BorderLayout.WEST);
    }


    private void addChatComponents()
    {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanelScrollPane = new JScrollPane(messagePanel);
        messagePanelScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messagePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanelScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messagePanelScrollPane.getViewport().addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                revalidate();
                repaint();
            }
        });

        chatPanel.add(messagePanelScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10,10,10,10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        //where messages will be typed
        JTextField inputField = new JTextField();
        //put a keylogger on the inputField
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            //keyTyped will invoke if the keylogger detects something is typed
            public void keyTyped(KeyEvent e) {
                //if enter is hit after the message is typed...
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    //collect the input from inputField and store it into a string called input
                    String input = inputField.getText();
                    //if the input string is empty or there is a bunch of spaces with no text...
                    //don't send the message
                    if (input.isEmpty() || input.isBlank()) return;
                    inputField.setText("");


                    //behind the gui, a StompClient will be created that will invoke its SendMessage method...
                    //a new message object will be created that will contain the username of the person sending the message...
                    //and the input string that the message itself is stored in from the inputField
                    //the SendMessage method will send the message object to the websocket server
                    myStompClient.SendMessage(new Message(username, input));
                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(0,10,0,10));
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

        JLabel messageLabel = new JLabel();
        messageLabel.setText("<html>" +
                "<body style='width:" + (0.60 * getWidth()) + "'px>" +
                    message.getMessage() +
                "</body>"+
        "</html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);
        //System.out.println(messageLabel.getText());

        return chatMessage;
    }

    @Override
    public void onMessageRecieve(Message message)
    {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();

        messagePanelScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> users)
    {
        if(connectedUsersPanel.getComponents().length >= 2)
        {
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        for(String user : users)
        {
            JLabel username = new JLabel();
            username.setText(user);
            username.setForeground(Utilities.TEXT_COLOR);
            username.setFont(new Font("Inter", Font.BOLD, 16));
            userListPanel.add(username);
        }

        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }

}
