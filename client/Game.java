package client;

import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tank_lib.Bullet;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.map_lib.TileTypes;
import tank_lib.network.BattlePacket;
import tank_lib.network.PacketTypes;

/**
 * Rappresenta e gestisce la logica del gioco e il game loop.
 */
public class Game extends Thread {
    BattleFrame battleFrame;
    Map map;
    Tank p1;
    Tank p2;
    ArrayList<Bullet> bullets1;
    ArrayList<Bullet> bullets2;

    ThreadNetwork threadNetwork;

    /**
     * Costruttore paramentrico
     * 
     * @param battleFrame   Finestra di gioco.
     * @param map           Oggetto Map che rappresenta la mappa.
     * @param p1            Tank che rappresenta il p1 (sempre questo client).
     * @param p2            Tank che rappresenta il p2 (sempre l'avversario).
     * @param threadNetwork ThreadNetwork che gestisce la comunicazione con il
     *                      server.
     */
    public Game(BattleFrame battleFrame, Map map, Tank p1, Tank p2, ThreadNetwork threadNetwork) {
        this.battleFrame = battleFrame;
        this.map = map;
        this.p1 = p1;
        this.p2 = p2;
        this.bullets1 = new ArrayList<>(); 
        this.bullets2 = new ArrayList<>(); 

        this.threadNetwork = threadNetwork;
    }

    /**
     * Fa partitre il game loop e gestisce la logica del gioco.
     */
    public void run() {
        // Initialize game variables and objects

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        this.threadNetwork.start();
        // Start the game loop
        while (true) {
            // based on packets received, update the game
            ArrayList<BattlePacket> packet = this.threadNetwork.getPacketsReceived();
            // foreach packet, process it
            for (BattlePacket battlePacket : packet) {
                handlePacket(battlePacket);
            }

            // move the tank if necessary
            KeyEvent k = this.battleFrame.getLastEvent();
            if (k == null) {
                continue;
            }
            // System.out.println(k.getKeyChar());
            if (k.getKeyChar() == 'w' || k.getKeyChar() == 'a' || k.getKeyChar() == 's' || k.getKeyChar() == 'd') {
                handleMovement(k);
                sendMovement(k);
                handleClipping();
            }
            
            if(k.getKeyChar() == 'z'){
                sendBullet();
            }
           

            // handle shooting

            // sends the packet to the server

        }

        // Clean up and exit the game
        // cleanup();
    }

    /**
     * Gestisce la collisone/clipping del tank per evitare che esca dalla mappa o
     * collida con gli edifici.
     */
    private void handleClipping() {
        // check if the tank is outside the map
        // sinistra
        if (p1.getPosition().getX() - p1.getWidth() / 2 < 0) {
            p1.getPosition().setX(p1.getPosition().getX() + 1);
        }
        // destra
        if (p1.getPosition().getX() + p1.getWidth() / 2 > battleFrame.getWidth()) {
            p1.getPosition().setX(p1.getPosition().getX() - 1);
        }
        // sopra
        if (p1.getPosition().getY() - p1.getWidth() / 2 < settings.TITLE_BAR_HEIGHT) {
            p1.getPosition().setY(p1.getPosition().getY() + 1);
        }
        // sotto
        if (p1.getPosition().getY() + p1.getWidth() / 2 > battleFrame.getHeight()) {
            p1.getPosition().setY(p1.getPosition().getY() - 1);
        }
        // check if the tank is inside a building
        // if it is, move it outside
        // if it is not, do nothing
        Point p = p1.getPositionInMap();
        // a destra c'è un edificio
        if (map.getTile(p.getX() + p1.getWidth() / 2, p.getY()).getTileType() == TileTypes.BUILDING)
            p1.getPosition().setX(p1.getPosition().getX() - 1);
        // a sinistra c'è un edificio
        if (map.getTile(p.getX() - p1.getWidth() / 2, p.getY()).getTileType() == TileTypes.BUILDING)
            p1.getPosition().setX(p1.getPosition().getX() + 1);
        // sopra c'è un edificio
        if (map.getTile(p.getX(), p.getY() - p1.getHeight() / 2).getTileType() == TileTypes.BUILDING)
            p1.getPosition().setY(p1.getPosition().getY() + 1);
        // sotto c'è un edificio
        if (map.getTile(p.getX(), p.getY() + p1.getHeight() / 2).getTileType() == TileTypes.BUILDING)
            p1.getPosition().setY(p1.getPosition().getY() - 1);
        System.out.println(p1.getPosition().getX() + " " + p1.getPosition().getY());
        System.out.println(map.getTile(p1.getPositionInMap()));

    }

    /**
     * Manda il movimento al server.
     * 
     * @param k Il KeyEvent che identifica il movimento.
     */
    private void sendMovement(KeyEvent k) {
        // send the movement to the server
        if (k == null)
            return;
        // tipo MOVM
        // 2 double per posizione, 1 per angolo
        byte[] bytes = new byte[Double.BYTES * 3];
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        byteBuf.putDouble(p1.getPosition().getX());
        byteBuf.putDouble(p1.getPosition().getY());
        byteBuf.putDouble(p1.getAngleRotationRadian());
        BattlePacket battlePacket = new BattlePacket(PacketTypes.MOVM, bytes);
        this.threadNetwork.addPacketToSend(battlePacket);
    }

    private void sendBullet() {
        // send the bullet to the server
        // tipo SHOT
        // 1 int per id, 2 double per posizione, 1 per angolo
        byte[] bytes = new byte[Integer.BYTES + Double.BYTES * 3];
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        byteBuf.putInt(p1.getBullet().getId());
        byteBuf.putDouble(p1.getPosition().getX());
        byteBuf.putDouble(p1.getPosition().getY());
        byteBuf.putDouble(p1.getAngleRotationRadian());
        BattlePacket battlePacket = new BattlePacket(PacketTypes.SHOT, bytes);
        this.threadNetwork.addPacketToSend(battlePacket);
    }

    /**
     * Gestisce il movimento del tank in base all'input.
     * 
     * @param k Il KeyEvent che identifica il movimento.
     */
    private void handleMovement(KeyEvent k) {
        // wasd movement
        float speedMultiplier = map.getTile(p1.getPositionInMap()).getSpeedMultiplier();
        float mov = (settings.TANK_SPEED_TILES_S * settings.TILE_SIZE_PX * speedMultiplier) * 1 / 30;
        // rotation per tick
        double rotation = 2 * Math.PI * settings.TANK_ROTATION_SPEED_RPM * 1 / 30;
        // System.out.println(mov);
        // System.out.println(rotation);
        if (k != null) {
            switch (k.getKeyChar()) {
                case 'w':
                    p1.moveBy(mov);
                    break;
                case 'a':
                    p1.rotateBy(rotation);
                    break;
                case 's':
                    mov *= -1;
                    p1.moveBy(mov);
                    break;
                case 'd':
                    rotation *= -1;
                    p1.rotateBy(rotation);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Gestisce il pacchetto ricevuto e aggiorna di conseguenza lo stato del gioco.
     * 
     * @param battlePacket Il BattlePacket ricevuto dal server.
     */
    private void handlePacket(BattlePacket battlePacket) {
        switch (battlePacket.getPacketType()) {
            case CONN:
                // set the player id to the one received
                break;
            case SMAP:
                // set the map to the one received
                break;
            case STRT:
                // startGame();
                break;
            case MOVM:
                // set the location of the tank to the one received
                System.out.println("MOVIMENTO NEMICO");
                ByteBuffer byteBuf = ByteBuffer.wrap(battlePacket.getPacketBytes());
                p2.setX(byteBuf.getDouble());
                p2.setY(byteBuf.getDouble());
                p2.setRotation(byteBuf.getDouble());
                break;
            case SHOT:
                // add a bullet to the list of bullets
                ByteBuffer byteBufSHOT = ByteBuffer.wrap(battlePacket.getPacketBytes());
                Bullet bullet = new Bullet(byteBufSHOT.getInt(), byteBufSHOT.getDouble(), byteBufSHOT.getDouble(),byteBufSHOT.getDouble());
                bullets1.add(bullet);
                break;
            case BDST:
                // remove the bullet from the list of bullets and destroy the building
                break;
            case HLTH:
                // update the tank health
                break;
            case GEND:
                // showEndGame();
                break;
            default:
                System.out.println("Packet type not recognized");
                break;
        }
    }

    private void update() {
        // Update game logic
    }

    // private void render() {
    // // Render game graphics
    // }

    // private void cleanup() {
    // // Clean up and exit the game
    // }
}
