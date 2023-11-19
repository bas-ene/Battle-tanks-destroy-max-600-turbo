package client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import tank_lib.network.BattlePacket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadNetwork extends Thread {
    ConcurrentLinkedQueue<BattlePacket> packetsToSend = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<BattlePacket> packetsReceived = new ConcurrentLinkedQueue<>();
    Socket socket;
    OutputStream outputStream;
    InputStream inputStream;

    public ThreadNetwork(Socket socket) {
        this.socket = socket;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[2048];
        while (true) {
            // manda pacchetti
            BattlePacket battlePacket = packetsToSend.poll();
            if (battlePacket != null) {
                // Get the byte array from the BattlePacket
                byte[] packetBytes = battlePacket.bitify();

                try {
                    // Send the byte array through the socket's output stream
                    outputStream.write(packetBytes);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // riccevi pacchetti
            // try {
            // int bytesRead = inputStream.read(buffer);
            // if (bytesRead != -1) {
            // byte[] packetBytes = Arrays.copyOf(buffer, bytesRead);
            // BattlePacket receivedPacket = new BattlePacket(packetBytes); // replace with
            // actual constructor
            // addPacketReceived(receivedPacket);
            // }
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }
    }

    public void addPacketToSend(BattlePacket packet) {
        packetsToSend.add(packet);
    }

    public void addPacketReceived(BattlePacket packet) {
        packetsReceived.add(packet);
    }

    public BattlePacket getPacketReceived() {
        return packetsReceived.poll();
    }

    public ArrayList<BattlePacket> getPacketsReceived() {
        ArrayList<BattlePacket> packets = new ArrayList<>();
        while (packetsReceived.peek() != null) {
            packets.add(packetsReceived.poll());
        }
        return packets;
    }
}
