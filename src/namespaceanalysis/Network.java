/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namespaceanalysis;
import java.util.*;
/**
 *
 * @author MohammadHossein
 */
public class Network {
    private List<PacketFace> arrivingPackets;
    private List<PacketFace> leavingPackets;

    private Map<String, List<String>> links;
        
    private Map<String, Node> face2Node; // face IDs are unique

    public Network() {
        this.arrivingPackets = new ArrayList<>();
        this.leavingPackets = new ArrayList<>();
        this.links = new HashMap<>();
        this.face2Node = new HashMap<>();
    }

    public List<PacketFace> getArrivingPackets() {
        return arrivingPackets;
    }

    public void setArrivingPackets(List<PacketFace> arrivingPackets) {
        this.arrivingPackets = arrivingPackets;
    }

    public List<PacketFace> getLeavingPackets() {
        return leavingPackets;
    }

    public void setLeavingPackets(List<PacketFace> leavingPackets) {
        this.leavingPackets = leavingPackets;
    }


    public Map<String, Node> getFace2Node() {
        return face2Node;
    }

    public void setFace2Node(Map<String, Node> face2Node) {
        this.face2Node = face2Node;
    }
    
    public void topologyTransferUpdates(){ // bring leaving packets to arriving positions
        List<PacketFace> temp_pf = new ArrayList<>();
        for(PacketFace l_packetFace: leavingPackets){
            SinglePacket l_packet = l_packetFace.getPacket();
            String l_face = l_packetFace.getFace();
            if(links.containsKey(l_face)){
                for(String a_face:links.get(l_face)){
                    PacketFace a_packetFace = new PacketFace(l_packet, a_face);
                    if(a_packetFace.getPacket().getName().getComponentByIndex(0).length()>0){
                        arrivingPackets.add(a_packetFace);
                        face2Node.get(a_face).addVisitedName(a_packetFace.getPacket().getNameAsString());
                    }
                }
            }
            temp_pf.add(l_packetFace);
        }
        for(PacketFace pf: temp_pf){
            leavingPackets.remove(pf);
        }
    }
    
    public void networkTransferUpdate(){//give arriving apckets to nodes
        List<PacketFace> temp_pf = new ArrayList<>();
        for(PacketFace a_packetFace: arrivingPackets){
            SinglePacket a_packet = a_packetFace.getPacket();
            String a_face = a_packetFace.getFace();
            if(face2Node.containsKey(a_face)){
                Node node = face2Node.get(a_face);
                List<PacketFace> l_packets = node.nodeTransfer(a_packetFace);
                for(PacketFace l_packet: l_packets){
                    if(l_packet.getPacket().getName().getComponentByIndex(0).length()>0){
                      leavingPackets.add(l_packet);
                      face2Node.get(a_face).addVisitedName(l_packet.getPacket().getNameAsString());
                    }
                }
            }            
            temp_pf.add(a_packetFace);
        }
        for(PacketFace pf: temp_pf){
            arrivingPackets.remove(pf);
        }
        
    }
    
    public void addLinks(String f1, String f2){
        if(links.containsKey(f1)){
            List <String> f1_tos = links.get(f1);
            if(!f1_tos.contains(f2)){
                f1_tos.add(f2);
            }
            links.put(f1, f1_tos);
        }
        else{
            List <String> f1_tos = new ArrayList<>();
            f1_tos.add(f2);
            links.put(f1, f1_tos);
        }
    }
    
    public void addBiLink(String f1, String f2){
        addLinks(f1,f2);
        addLinks(f2,f1);
    }
    
    public void addArrivingPackets(PacketFace pf){
        arrivingPackets.add(pf);
    }
    
    public void addLeavingPackets(PacketFace pf){
        leavingPackets.add(pf);
    }
    
    public void addFace2Node(String face, Node node){
        if(!face2Node.containsKey(face)){
            face2Node.put(face, node);
        }
    }
    
    public void printArrivingPacketLists(){
        System.out.println("Arriving Packets:");
        for(PacketFace pf: arrivingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }
    
    public void printLeavingPacketLists(){
        System.out.println("Leaving Packets:");
        for(PacketFace pf: leavingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }
    
    public void printPacketLists(){
        for(PacketFace pf: leavingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
        for(PacketFace pf: arrivingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }
    
    public void printProviderNames(){
        System.out.println("\nAnnounced Provider Names:");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: face2Node.values()){
           if(!nodeNames.contains(n.getNodeID())){ 
            n.printProviderNames();
            nodeNames.add(n.getNodeID());
            System.out.println();
           }
        }
    }
    
    public void printVisitedNames(){
        System.out.println("\nAll Visited Names at nodes:");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: face2Node.values()){
           if(!nodeNames.contains(n.getNodeID())){ 
            n.printVisitedNames();
            nodeNames.add(n.getNodeID());
            System.out.println();
           }
        }       
    }
}
