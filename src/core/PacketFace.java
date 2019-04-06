
package core;
import java.util.*;
/**
 * packet and face pair
 * @author Mohammad Jahanian
 */

public class PacketFace {
    private Packet packet;
    private String face;

    public PacketFace(Packet packet, String face) {
        this.packet = packet;
        this.face = face;
    }

    public PacketFace() {
        this.packet = new Packet();
        this.face = "";
    }
    
    public PacketFace(String packet, String face) {
        this.packet = new Packet(packet);
        this.face = face;
    }
    
    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String packetFace2String(){
        return this.packet.getNameAsString()+" ("+this.face+")";
    }
    
}
