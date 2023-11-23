package client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class EndGameFrame extends JFrame {
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

        setVisible(true);
    }
}
