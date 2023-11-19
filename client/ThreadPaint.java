package client;

public class ThreadPaint extends Thread{
    BattleFrame battleFrame;

    public ThreadPaint(BattleFrame battleFrame) {
        this.battleFrame = battleFrame;
    }

    @Override
    public void run() {
        while (true) {
            // refresha il frame
            battleFrame.paint(battleFrame.getGraphics());

            // 30 FPS
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
