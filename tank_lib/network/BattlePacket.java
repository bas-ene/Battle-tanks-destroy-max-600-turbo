package tank_lib.network;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class BattlePacket {
    PacketTypes packetType;
    byte[] packetBytes;

    public BattlePacket(PacketTypes packetType) {
        this.packetType = packetType;
    }

    public BattlePacket(PacketTypes packetType, byte[] packetBytes) {
        this.packetType = packetType;
        this.packetBytes = packetBytes;
    }

    public BattlePacket(byte[] packetBytes) {
        this.packetBytes = packetBytes;
    }

    public PacketTypes getPacketType() {
        return packetType;
    }

    public byte[] bitify() {
        // 4 per la lunghezza del messaggio, 4 byte per il tipo di pacchetto, il
        // restante per i dati
        byte bytes[] = new byte[4 + 4 + packetBytes.length];
        // lunghezza del messaggio - questa lunghezza
        ByteBuffer b = ByteBuffer.wrap(bytes);
        b.putInt(packetBytes.length);
        // inserisco il tipo di pacchetto
        b.put(packetType.toString().getBytes());
        // dati
        b.put(packetBytes);

        return bytes;
    }

    public byte[] getPacketBytes() {
        return packetBytes;
    }

}
