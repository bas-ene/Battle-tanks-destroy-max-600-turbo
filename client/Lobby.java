package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lobby extends JFrame {
    private JTextField serverIpTextField;
    private JTextField serverPortTextField;
    private JTextField usernameTextField;
    private JButton connectButton;
    private String ipString;
    private String portString;
    private String username;

    public Lobby(String ipString, String portString, String username) {
        setTitle("Lobby");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));
        setResizable(false);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverIpTextField = new JTextField(ipString, 15);
        serverPortTextField = new JTextField(portString, 5);
        usernameTextField = new JTextField(username, 10);
        connectButton = new JButton("Connect");

        connectButton.addActionListener((e) -> {
            setVars();
            setVisible(false);
        });

        add(new JLabel("Server IP:"));
        add(serverIpTextField);
        add(new JLabel("Server Port:"));
        add(serverPortTextField);
        add(new JLabel("Username:"));
        add(usernameTextField);
        add(connectButton);
        pack();
        setVisible(true);
    }

    private void setVars() {
        ipString = serverIpTextField.getText();
        portString = serverPortTextField.getText();
        username = usernameTextField.getText();
    }

    public String getIpString() {
        return ipString;
    }

    public String getPortString() {
        return portString;
    }

    public String getUsername() {
        return username;
    }

}
