package client;

import tank_lib.settings;

/**
 * ThreadPaint
 */
public class ThreadPaint extends Thread {
    BattleFrame battleFrame;
    Game game;

    /**
     * Costruttore parametrico
     * 
     * @param battleFrame Finestra di gioco.
     */
    public ThreadPaint(BattleFrame battleFrame, Game game) {
        this.battleFrame = battleFrame;
        this.game = game;
    }

    /**
     * Ogni {@link settings#REFRESH_RATE} millisecondi ridipingi il frame, fino a
     * che {@link Game#isGameRunning} è true.
     */
    @Override
    public void run() {
        while (game.isGameRunning) {
            // refresh the frame
            battleFrame.repaint();

            // 30 FPS
            try {
                Thread.sleep(settings.REFRESH_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
