package tank_lib.network;

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
        return packetBytes;
    }

}
