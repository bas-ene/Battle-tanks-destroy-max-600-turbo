package client;

import java.awt.event.KeyEvent;
import java.net.Socket;
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
    int playerID;
    int winnerID;
    String username;
    Tank[] players;
    ArrayList<Bullet> bullets1;
    ArrayList<Bullet> bullets2;
    ThreadNetwork threadNetwork;
    ThreadPaint threadPaint;
    boolean isGameRunning = false;

    /**
     * Costruttore paramentrico, usato principalmente per test.
     * 
     * @param battleFrame   Finestra di gioco.
     * @param map           Oggetto Map che rappresenta la mappa.
     * @param player        Tank che rappresenta il player (sempre questo
     *                      client).
     * @param p2            Tank che rappresenta il p2 (sempre l'avversario).
     * @param threadNetwork ThreadNetwork che gestisce la comunicazione con il
     *                      server.
     */
    public Game(BattleFrame battleFrame, Map map, Tank player, Tank p2, ThreadNetwork threadNetwork) {
        this.battleFrame = battleFrame;
        this.map = map;
        this.playerID = 0;
        this.players = new Tank[2];
        players[0] = player;
        players[1] = player;
        this.bullets1 = new ArrayList<>();
        this.bullets2 = new ArrayList<>();

        this.threadNetwork = threadNetwork;
    }

    /**
     * Costruttore parametrico, che riceve la mappa dal server e si crea da solo il
     * BattleFrame, il ThreadPaint associato e i Tank.
     * 
     * @param threadNetwork Thread per la comunicazione con il server.
     */
    public Game(Socket sockets, String username) {
        this.bullets1 = new ArrayList<>();
        this.bullets2 = new ArrayList<>();
        this.players = new Tank[settings.NUMBER_OF_CLIENTS];
        this.username = username;
        this.threadNetwork = new ThreadNetwork(sockets, this);
    }

    /**
     * Fa partitre il game loop e gestisce la logica del gioco.
     */
    public void run() {
        // Initialize game variables and objects
        // showLobby();
        this.threadNetwork.start();
        sendConnectionPacket(this.username);
        // System.out.println("CONNESSIONE INVIATA");
        // getIdFromServer();
        // System.out.println("ID RICEVUTI");
        // getMapFromServer();
        // System.out.println("MAPPA RICEVUTA");

        // wait for STRT packet
        System.out.println("IN ATTESA DI START");
        while (!isGameRunning) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("START RICEVUTO");
        // Start the game loop
        long timeLastPacketSent = System.currentTimeMillis();
        final int delay = 1000 / 25;
        while (isGameRunning) {

            // move the tank if necessary
            KeyEvent k = this.battleFrame.getLastEvent();
            if (k == null) {
                continue;
            }
            System.out.println(k.getKeyChar());
            if (k.getKeyChar() == 'w' || k.getKeyChar() == 'a' || k.getKeyChar() == 's' || k.getKeyChar() == 'd') {
                handleMovement(k);
                if (System.currentTimeMillis() - timeLastPacketSent < delay)
                    continue;
                else {
                    System.out.println("INVIO MOVIMENTO al secondo: " + System.currentTimeMillis() / 1000);
                    System.out.println(
                            "Tempo passato da ultimo invio: "
                                    + ((System.currentTimeMillis() - timeLastPacketSent) / 1000));
                    timeLastPacketSent = System.currentTimeMillis();
                    if (k.getKeyCode() == KeyEvent.VK_W || k.getKeyCode() == KeyEvent.VK_A
                            || k.getKeyCode() == KeyEvent.VK_S || k.getKeyCode() == KeyEvent.VK_D) {
                        sendMovement(k);
                    }
                }
                handleClipping();
            }

            if (k.getKeyChar() == 'z') {
                sendBullet();
            }

            // handle shooting

            // sends the packet to the server

        }
        System.out.println("FINE PARTITA");
        this.battleFrame.setVisible(false);
        this.battleFrame.dispose();
        // cleanup();
    }

    private void sendConnectionPacket(String username) {
        // send a connection packet to the server
        // tipo CONN
        byte[] bytes = username.getBytes();
        BattlePacket battlePacket = new BattlePacket(PacketTypes.CONN, bytes);
        this.threadNetwork.addPacketToSend(battlePacket);
    }

    /**
     * Gestisce la collisone/clipping del tank per evitare che esca dalla mappa o
     * collida con gli edifici.
     */
    private void handleClipping() {
        // check if the tank is outside the map
        // sinistra
        if (this.players[playerID].getPosition().getX() - this.players[playerID].getWidth() / 2 < 0) {
            this.players[playerID].getPosition().setX(this.players[playerID].getPosition().getX() + 1);
        }
        // destra
        if (this.players[playerID].getPosition().getX() + this.players[playerID].getWidth() / 2 > battleFrame
                .getWidth()) {
            this.players[playerID].getPosition().setX(this.players[playerID].getPosition().getX() - 1);
        }
        // sopra
        if (this.players[playerID].getPosition().getY()
                - this.players[playerID].getWidth() / 2 < settings.TITLE_BAR_HEIGHT) {
            this.players[playerID].getPosition().setY(this.players[playerID].getPosition().getY() + 1);
        }
        // sotto
        if (this.players[playerID].getPosition().getY() + this.players[playerID].getWidth() / 2 > battleFrame
                .getHeight()) {
            this.players[playerID].getPosition().setY(this.players[playerID].getPosition().getY() - 1);
        }
        // check if the tank is inside a building
        // if it is, move it outside
        // if it is not, do nothing
        Point p = this.players[playerID].getPositionInMap();
        // a destra c'è un edificio
        if (map.getTile(p.getX() + this.players[playerID].getWidth() / 2, p.getY())
                .getTileType() == TileTypes.BUILDING)
            this.players[playerID].getPosition().setX(this.players[playerID].getPosition().getX() - 1);
        // a sinistra c'è un edificio
        if (map.getTile(p.getX() - this.players[playerID].getWidth() / 2, p.getY())
                .getTileType() == TileTypes.BUILDING)
            this.players[playerID].getPosition().setX(this.players[playerID].getPosition().getX() + 1);
        // sopra c'è un edificio
        if (map.getTile(p.getX(), p.getY() - this.players[playerID].getHeight() / 2)
                .getTileType() == TileTypes.BUILDING)
            this.players[playerID].getPosition().setY(this.players[playerID].getPosition().getY() + 1);
        // sotto c'è un edificio
        if (map.getTile(p.getX(), p.getY() + this.players[playerID].getHeight() / 2)
                .getTileType() == TileTypes.BUILDING)
            this.players[playerID].getPosition().setY(this.players[playerID].getPosition().getY() - 1);
        System.out.println(this.players[playerID].getPosition().getX() + " "
                + this.players[playerID].getPosition().getY());
        System.out.println(map.getTile(this.players[playerID].getPositionInMap()));

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
        byte[] bytes = new byte[Integer.BYTES + Double.BYTES * 3];
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        byteBuf.putInt(this.playerID);
        byteBuf.putDouble(this.players[playerID].getPosition().getX());
        byteBuf.putDouble(this.players[playerID].getPosition().getY());
        byteBuf.putDouble(this.players[playerID].getAngleRotationRadian());
        BattlePacket battlePacket = new BattlePacket(PacketTypes.MOVM, bytes);
        this.threadNetwork.addPacketToSend(battlePacket);
    }

    private void sendBullet() {
        // send the bullet to the server
        // tipo SHOT
        // 1 int per id, 2 double per posizione, 1 per angolo
        byte[] bytes = new byte[Integer.BYTES + Double.BYTES * 3];
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        byteBuf.putInt(this.players[playerID].getBullet().getId());
        byteBuf.putDouble(this.players[playerID].getPosition().getX());
        byteBuf.putDouble(this.players[playerID].getPosition().getY());
        byteBuf.putDouble(this.players[playerID].getAngleRotationRadian());
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
        float speedMultiplier = map.getTile(this.players[playerID].getPositionInMap()).getSpeedMultiplier();
        float mov = (settings.TANK_SPEED_TILES_S * settings.TILE_SIZE_PX * speedMultiplier) * 1 / 30;
        // rotation per tick
        double rotation = 2 * Math.PI * settings.TANK_ROTATION_SPEED_RPM * 1 / 30;
        if (k != null) {
            switch (k.getKeyChar()) {
                case 'w':
                    this.players[playerID].moveBy(mov);
                    break;
                case 'a':
                    this.players[playerID].rotateBy(rotation);
                    break;
                case 's':
                    mov *= -1;
                    this.players[playerID].moveBy(mov);
                    break;
                case 'd':
                    rotation *= -1;
                    this.players[playerID].rotateBy(rotation);
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
    void handlePacket(BattlePacket battlePacket) {
        switch (battlePacket.getPacketType()) {
            case CONN:
                // set the player id to the one received
                System.out.println("RICEVUTI ID");

                ByteBuffer byteBufCONN = ByteBuffer.wrap(battlePacket.getPacketBytes());
                // set the id of the player
                this.playerID = byteBufCONN.getInt();
                for (int i = 0; i < settings.NUMBER_OF_CLIENTS; i++) {
                    if (i == this.playerID)
                        players[i] = new Tank(username, playerID);
                    else
                        players[i] = new Tank("Nemico " + i, byteBufCONN.getInt());
                }
                break;
            case SMAP:
                // set the map to the one received
                System.out.println("RICEVUTA MAPPA");

                ByteBuffer byteBufSMAP = ByteBuffer.wrap(battlePacket.getPacketBytes());
                int mapWidth = byteBufSMAP.getInt();
                int mapHeight = byteBufSMAP.getInt();

                byte[] mBytes = new byte[mapWidth * mapHeight];
                byteBufSMAP.get(mBytes);
                this.map = new Map(mBytes, mapHeight, mapWidth);
                // set spawn position based on id
                for (int i = 0; i < players.length; i++) {
                    byte[] spawnPointBytes = new byte[Double.BYTES * 2];
                    byteBufSMAP.get(spawnPointBytes);
                    this.players[i].setPositionInWindow(new Point(spawnPointBytes));
                }

                break;
            case STRT:
                // startGame();
                startGame();
                this.isGameRunning = true;
                break;
            case MOVM:
                // set the location of the tank to the one received
                System.out.println("MOVIMENTO NEMICO");

                ByteBuffer byteBufMOVM = ByteBuffer.wrap(battlePacket.getPacketBytes());
                int id = byteBufMOVM.getInt();
                double x = byteBufMOVM.getDouble();
                double y = byteBufMOVM.getDouble();
                double angle = byteBufMOVM.getDouble();
                this.players[id].setPosition(new Point(x, y));
                this.players[id].setRotation(angle);
                break;
            case SHOT:
                // add a bullet to the list of bullets
                System.out.println("SPARO");

                ByteBuffer byteBufSHOT = ByteBuffer.wrap(battlePacket.getPacketBytes());
                Bullet bullet = new Bullet(byteBufSHOT.getInt(), byteBufSHOT.getDouble(), byteBufSHOT.getDouble(),
                        byteBufSHOT.getDouble());
                bullets1.add(bullet);
                break;
            case BDST:
                // remove the bullet from the list of bullets and destroy the building
                break;
            case HLTH:
                ByteBuffer byteBufHTLH = ByteBuffer.wrap(battlePacket.getPacketBytes());
                // update the tank health
                int idTankHit = byteBufHTLH.getInt();
                float newHealth = byteBufHTLH.getFloat();
                players[idTankHit].setHealth(newHealth);
                break;
            case GEND:
                // showEndGame();
                ByteBuffer byteBufGEND = ByteBuffer.wrap(battlePacket.getPacketBytes());
                winnerID = byteBufGEND.getInt();
                isGameRunning = false;
                break;
            default:
                System.out.println("Packet type not recognized");
                break;
        }
    }

    private void startGame() {
        this.battleFrame = new BattleFrame(map, players, playerID);
        this.threadPaint = new ThreadPaint(battleFrame, this);
        threadPaint.start();
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public int getWinnerID() {
        return this.winnerID;
    }

    public int getPlayerID() {
        return this.playerID;
    }
}