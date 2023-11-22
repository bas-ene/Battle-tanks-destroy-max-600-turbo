package client;

import tank_lib.settings;

/**
 * ThreadPaint
 */
public class ThreadPaint extends Thread {
    BattleFrame battleFrame;

    /**
     * Costruttore parametrico
     * 
     * @param battleFrame Finestra di gioco.
     */
    public ThreadPaint(BattleFrame battleFrame) {
        this.battleFrame = battleFrame;
    }

    /**
     * Ogni {@link settings.REFRESH_RATE} millisecondi ridipingi il frame.
     */
    @Override
    public void run() {
        while (true) {
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
