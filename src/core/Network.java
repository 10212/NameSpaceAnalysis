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
public class Network {
    private List<PacketFace> arrivingPackets;
    private List<PacketFace> leavingPackets;

    private Map<String, List<String>> links;
        
    private Map<String, Node> face2Node; // face IDs are unique
    
    private int loops; // no. of loops detected

    public Network() {
        this.arrivingPackets = new ArrayList<>();
        this.leavingPackets = new ArrayList<>();
        this.links = new HashMap<>();
        this.face2Node = new HashMap<>();
        this.loops = 0;
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

    public int getLoops() {
        return loops;
    }
    
    
    public void topologyTransferUpdates(int logLoop){ // bring leaving packets to arriving positions
        List<PacketFace> temp_pf = new ArrayList<>();
        for(PacketFace l_packetFace: leavingPackets){
            SinglePacket l_packet = l_packetFace.getPacket();
            String l_face = l_packetFace.getFace();
            if(links.containsKey(l_face)){
                for(String a_face:links.get(l_face)){
                    PacketFace a_packetFace = new PacketFace(l_packet, a_face);
                    //Loop Detection ...................
                    if(face2Node.get(a_face).getArrivingVisitedNames().contains(a_packetFace.getPacket().getNameAsString())){
                        if(logLoop==1)
                            //System.out.println("Loop detected! for "+a_packetFace.getPacket().getNameAsString()+" @ "+face2Node.get(a_face).getNodeID());
                            System.out.println("\tLoop\t"+a_packetFace.getPacket().getNameAsString()+"\t"+face2Node.get(a_face).getNodeID());                        
                        loops++;
                        continue;
                    }
                    //...............................
                    else if(a_packetFace.getPacket().getName().getComponentByIndex(0).length()>0){
                        arrivingPackets.add(a_packetFace);
                        face2Node.get(a_face).addVisitedName(a_packetFace.getPacket().getNameAsString());
                        face2Node.get(a_face).addArrivingVisitedName(a_packetFace.getPacket().getNameAsString());
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
            temp_pf.add(a_packetFace);
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
        System.out.println("\nArriving Packets:");
        for(PacketFace pf: arrivingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }
    
    public void printLeavingPacketLists(){
        System.out.println("\nLeaving Packets:");
        for(PacketFace pf: leavingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }
    
    public void printPacketLists(){
        //System.out.println("leaving:");
        for(PacketFace pf: leavingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
        //System.out.println("arriving:");
        for(PacketFace pf: arrivingPackets){
            System.out.println("\t"+pf.packetFace2String());
        }
    }

    public void printNodeRules(){
        System.out.println("\nNode rules:");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: face2Node.values()){
           if(!nodeNames.contains(n.getNodeID())){ 
            System.out.print(n.getNodeID()+": ");
            for(Rule r: n.getRules()){
                r.printRule();
            }
            nodeNames.add(n.getNodeID());
            System.out.println();
           }
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
    
    public void printArrivingVisitedNames(){
        System.out.println("\nAll Arriving Visited Names at nodes:");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: face2Node.values()){
           if(!nodeNames.contains(n.getNodeID())){ 
            n.printArrivingVisitedNames();
            nodeNames.add(n.getNodeID());
            System.out.println();
           }
        }       
    }
    
    public void printLeavingArrivingVisitedNames(){
        System.out.println("\nAll Leaving Visited Names at nodes:");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: face2Node.values()){
           if(!nodeNames.contains(n.getNodeID())){ 
            n.printLeavingVisitedNames();
            nodeNames.add(n.getNodeID());
            System.out.println();
           }
        }       
    }
    
    public void printFace2Node(){
        System.out.println("\nFace 2 Node Maps:");
        for(String f1: face2Node.keySet()){
            System.out.print(f1+":"+face2Node.get(f1).getNodeID()+ ", ");
        }
        System.out.print("\n");
    }
    
    
    public void printLinks(){
        System.out.println("\nLink adjacency list:");
        for(String f1: links.keySet()){
            System.out.println(f1+" -> " +links.get(f1));
        }
    }
    
    public void printNetworkSummary(){
        this.printFace2Node();
        this.printLinks();
        this.printNodeRules();
        this.printProviderNames();
        this.printVisitedNames();
        this.printArrivingPacketLists();
        this.printLeavingPacketLists();
    }
}
