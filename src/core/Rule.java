
package core;
import core.Common;
/**
 * each rule is a single network transfer function
 * @author Mohammad Jahanian
 */
public class Rule {
    private Packet packetRule;
    private String face_in; // "ANY" means no condition on incoming face
    private String face_out;

    public Rule() {
        this.packetRule = new Packet();
        this.face_in = "ANY";
        this.face_out = "";
    }    
    
    public Rule(Packet packetRule, String face_in, String face_out) {
        this.packetRule = packetRule;
        this.face_in = face_in;
        this.face_out = face_out;
    }
    
    public Rule(String packetRule, String face_in, String face_out) {
        this.packetRule = new Packet(packetRule);
        this.face_in = face_in;
        this.face_out = face_out;
    }

    public Packet getPacketRule() {
        return packetRule;
    }

    public void setPacketRule(Packet packetRule) {
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
        Common nsa = new Common();
        if(matchInFace(packetFace_in)){
            Packet packet_out = nsa.singlePacketIntersection(packetFace_in.getPacket(), packetRule);
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
