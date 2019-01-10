/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;
import java.util.*;
/**
 *
 * @author MohammadHossein
 */
//packet and face pair
public class PacketFace {
    private SinglePacket packet;
    private String face;

    public PacketFace(SinglePacket packet, String face) {
        this.packet = packet;
        this.face = face;
    }

    public PacketFace() {
        this.packet = new SinglePacket();
        this.face = "";
    }
    
    public PacketFace(String packet, String face) {
        this.packet = new SinglePacket(packet);
        this.face = face;
    }
    
    public SinglePacket getPacket() {
        return packet;
    }

    public void setPacket(SinglePacket packet) {
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
