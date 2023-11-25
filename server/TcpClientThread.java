package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;
import tank_lib.network.PacketTypes;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * La classe rappresenta un thread di rete che gestisce l'invio e la ricezione
 * di {@link BattlePacket} su una socket.
 */
public class TcpClientThread extends Thread {
    private ConcurrentLinkedQueue<BattlePacket> packetsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<BattlePacket> packetsReceived = new ConcurrentLinkedQueue<>();
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * Constructs a TcpClientThread object with the specified socket.
     *
     * @param socket The socket to communicate over.
     */
    public TcpClientThread(Socket socket) {
        this.socket = socket;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * La logica del thread.
     * Crea due thread, uno per ascoltare e uno per mandare.
     * Mette in coda i pacchetti ricevuti e li rimuove quando vengono letti.
     * Riceve dal server i pacchetti da mandare e li rimuove quando vengono mandati.
     */
    @Override
    public void run() {
        // init

        // Create a thread for sending packets
        Thread sendThread = new Thread(() -> {
            while (true) {
                BattlePacket battlePacket = packetsToSend.poll();
                if (battlePacket != null) {
                    byte[] packetBytes = battlePacket.bitify();

                    try {
                        outputStream.write(packetBytes);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                }
            }
        });

        // Create a thread for receiving packets
        Thread receiveThread = new Thread(() -> {
            while (true) {
                try {
                    // parsing
                    byte[] pLengthBytes = new byte[4];
                    inputStream.read(pLengthBytes);
                    int packetLength = ByteBuffer.wrap(pLengthBytes).getInt();
                    System.out.println("Received packet length: " + packetLength);

                    byte[] strBytes = new byte[4];
                    inputStream.read(strBytes);
                    String typeBytes = new String(strBytes);
                    System.out.println("Received packet type: " + typeBytes);
                    PacketTypes type = PacketTypes.valueOf(typeBytes);

                    byte[] dataBytes = new byte[packetLength];
                    inputStream.read(dataBytes);
                    BattlePacket p = new BattlePacket(type, dataBytes);
                    addPacketReceived(p);
                    sleep(1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        sendThread.start();
        receiveThread.start();

        // Wait for both threads to finish
        try {
            sendThread.join();
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiungi un BattlePacket alla coda packetsToSend.
     * 
     * @param packet Il BattlePacket da inviare.
     */
    public void addPacketToSend(BattlePacket packet) {
        packetsToSend.add(packet);
    }

    /**
     * Aggiunge un BattlePacket alla coda packetsReceived.
     * 
     * @param packet Il BattlePacket ricevuto.
     */
    public void addPacketReceived(BattlePacket packet) {
        packetsReceived.add(packet);
    }

    /**
     * Ritorna e rimuove il prossimo BattlePacket dalla coda packetsReceived.
     * 
     * @return IL prossimo BattlePacket ricevuto, o null se la coda è vuota.
     */
    public BattlePacket getPacketReceived() {
        return packetsReceived.poll();
    }

    /**
     * Aspetta fino a che non riceve un pacchetto di tipo CONN.
     * 
     * @return Lo username del client.
     */
    public String getTankUsername() {

        while (true) {
            BattlePacket packet = getPacketReceived();
            if (packet != null && packet.getPacketType() == PacketTypes.CONN) {
                return new String(packet.getPacketBytes());
            }

        }
    }

    /**
     * Invia un pacchetto di tipo {@link PacketTypes#STRT}.
     */
    public void sendStartPacket() {
        BattlePacket packet = new BattlePacket(PacketTypes.STRT, null);
        addPacketToSend(packet);
    }

    /**
     * Invia gli ID ai client.
     * 
     * @param i                 L'id del client di cui inviare gli ID.
     * @param NUMBER_OF_CLIENTS Il numero totale di client.
     */
    public void sendIDs(int i, int NUMBER_OF_CLIENTS) {
        // packet of type CONN, first puts the ID of the tank of interest, and then the
        // others in ascending order
        ByteBuffer byteBuf = ByteBuffer.allocate(NUMBER_OF_CLIENTS * Integer.BYTES);
        byteBuf.putInt(i);
        for (int j = 0; j < NUMBER_OF_CLIENTS; j++) {
            if (j != i) {
                byteBuf.putInt(j);
            }
        }
        BattlePacket packet = new BattlePacket(PacketTypes.CONN, byteBuf.array());
        addPacketToSend(packet);
    }

    /**
     * Invia la mappa al client.
     * 
     * @param map The map to send.
     */
    public void sendMap(Map map) {
        BattlePacket packet = new BattlePacket(PacketTypes.SMAP, map.bitify());
        addPacketToSend(packet);
    }
}
