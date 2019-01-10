/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;
import core.NameSpaceAnalysis;
/**
 *
 * @author MohammadHossein
 */
public class Rule {
    private SinglePacket packetRule;
    private String face_in; // "ANY" means no condition on incoming face
    private String face_out;

    public Rule() {
        this.packetRule = new SinglePacket();
        this.face_in = "ANY";
        this.face_out = "";
    }    
    
    public Rule(SinglePacket packetRule, String face_in, String face_out) {
        this.packetRule = packetRule;
        this.face_in = face_in;
        this.face_out = face_out;
    }
    
    public Rule(String packetRule, String face_in, String face_out) {
        this.packetRule = new SinglePacket(packetRule);
        this.face_in = face_in;
        this.face_out = face_out;
    }

    public SinglePacket getPacketRule() {
        return packetRule;
    }

    public void setPacketRule(SinglePacket packetRule) {
        this.packetRule = packetRule;
    }

    public String getFace_in() {
        return face_in;
    }

    public void setFace_in(String face_in) {
        this.face_in = face_in;
    }

    public String getFace_out() {
        return face_out;
    }

    public void setFace_out(String face_out) {
        this.face_out = face_out;
    }

    
    
    public boolean matchInFace(PacketFace packetFace_in){
        if(packetFace_in.getFace().equals(face_in) || face_in.equals("ANY")){
            return true;
        }
        return false;
    }
    
    public PacketFace matchRule(PacketFace packetFace_in){
        NameSpaceAnalysis nsa = new NameSpaceAnalysis();
        if(matchInFace(packetFace_in)){
            SinglePacket packet_out = nsa.singlePacketIntersection(packetFace_in.getPacket(), packetRule);
            PacketFace result = new PacketFace(packet_out, face_out);
            return result;
        }
        else{
            PacketFace result = new PacketFace();
            return result;
        }
    }
    
    public void printRule(){
        System.out.println(packetRule.getNameAsString()+" "+face_in+" "+face_out);
    }
}
