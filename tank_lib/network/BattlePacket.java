package tank_lib.network;

import java.nio.ByteBuffer;

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
        int packetBytesLength = packetBytes == null ? 0 : packetBytes.length;
        byte bytes[] = new byte[4 + 4 + packetBytesLength];
        // lunghezza del messaggio - questa lunghezza
        ByteBuffer b = ByteBuffer.wrap(bytes);
        b.putInt(packetBytesLength);
        // inserisco il tipo di pacchetto
        b.put(packetType.toString().getBytes());
        // dati
        if (packetBytes != null)
            b.put(packetBytes);
        return bytes;
    }

    public byte[] getPacketBytes() {
        return packetBytes;
    }

}
