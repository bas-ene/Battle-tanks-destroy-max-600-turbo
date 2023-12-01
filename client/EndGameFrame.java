package client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Rappresenta il frame che viene mostrato alla fine della partita, mostrando se
 * il giocatore ha vinto o perso.
 */
public class EndGameFrame extends JFrame {
    /**
     * Costruttore parametrico.
     * 
     * @param winnerId L'ID del vincitore.
     * @param playerId L'ID del client che ha aperto il frame.
     */
    public EndGameFrame(int winnerId, int playerId) {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(300, 200);

        JLabel label = new JLabel();
        if (winnerId == playerId) {
            label.setText("You won!");
            label.setForeground(Color.GREEN);

        } else {
            label.setText("You lost!");
            label.setForeground(Color.RED);
        }
        add(label);
    }
}
