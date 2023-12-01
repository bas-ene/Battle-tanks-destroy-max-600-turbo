package client;

import javax.swing.*;
import java.awt.*;

/**
 * Rappresetna una simil-lobby dove l'utente può inserire l'IP del server, la
 * porta e il suo username per connettersi alla lobby.
 */
public class LobbyFrame extends JFrame {
    private JTextField serverIpTextField;
    private JTextField serverPortTextField;
    private JTextField usernameTextField;
    private JButton connectButton;
    private String ipString;
    private String portString;
    private String username;

    /**
     * Costruisci una lobby con i valori iniziali per IP, porta e username.
     * 
     * @param ipString   il valore iniziale per l'IP del server.
     * @param portString il valore initiale per la porta del server.
     * @param username   il valore iniziale per lo username.
     */
    public LobbyFrame(String ipString, String portString, String username) {
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
    }

    /**
     * Setta i valori di ipString, portString e username in base al testo inserito,
     * viene chiamato quando l'utente preme il bottone "Connect".
     */
    private void setVars() {
        ipString = serverIpTextField.getText();
        portString = serverPortTextField.getText();
        username = usernameTextField.getText();
    }

    /**
     * Ritora l'indirizzo IP del server inserito dall'utente.
     * 
     * @return L'indirizzo IP del server.
     */
    public String getIpString() {
        return ipString;
    }

    /**
     * Ritorna la porta del server inserita dall'utente.
     *
     * @return la porta del server.
     */
    public String getPortString() {
        return portString;
    }

    /**
     * Ritorna lo username inserito dall'utente.
     *
     * @return lo username.
     */
    public String getUsername() {
        return username;
    }
}
