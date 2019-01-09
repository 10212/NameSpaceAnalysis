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
public class example1 {
    
    public static void main(String[] args) {
        NameSpaceAnalysis nsa = new NameSpaceAnalysis();

        Name n1 = new Name ("/a/b/c/*");
        System.out.println(n1.getComponentByIndex(0));
        System.out.println(n1.getComponentByIndex(1));
        System.out.println(n1.getComponentByIndex(2));
        System.out.println(n1.getComponentByIndex(3));
        System.out.println(n1.name2String());
        Name n2 = new Name ("/a/b/f/d/*");
        System.out.println(n2.name2String());
        System.out.println(nsa.nameIntersection(n1,n2).name2String());

        
        Name name1 = new Name("/a/b/c/d/");
        Name name2 = new Name("/a/b/c/?/*");
        System.out.println(name1.name2String()+" subsetOf "+ name2.name2String()+ " ? "+ name1.subsetOf(name2));


        
        System.out.println("---------------");
        SinglePacket p1 = new SinglePacket ("/a/b/c/f/*");
        System.out.println(p1.getName().name2String());
        SinglePacket p2 = new SinglePacket ("/a/b/?/!d/!e/*");
        System.out.println(p2.getName().name2String());
        System.out.println(nsa.singlePacketIntersection(p1,p2).getName().name2String());
        
        
        
        System.out.println("---------------");   
        String s1 = "/g/b/y/*";
        
        String s2 = "/a/b/y/d/*";
        Rule rule1 = new Rule(s1,"ANY","f1");
        
        PacketFace pf1 = new PacketFace("/*", "f2");
        System.out.println(s2);
        System.out.println(rule1.matchInFace(pf1));
        System.out.println(rule1.matchRule(pf1).packetFace2String()); 
        
        System.out.println("---------------");   
        Node n = new Node("node1");
        //n.addRule(rule1);
        n.addRule("/y/c/*", "ANY", "f4");
        n.addRule("/g/c/*", "ANY", "f3");
        n.addRule("/a/b/y/*", "ANY", "f4");
        List<PacketFace> pf_out_list = n.nodeTransfer(pf1);
        System.out.println(nsa.packetFaceList2String(pf_out_list));
        
        
        System.out.println("---------------");
        Network net = new Network();
        net.addArrivingPackets(pf1);
        net.addFace2Node("f1", n);
        net.addFace2Node("f2", n);
        net.addFace2Node("f3", n);
        net.addFace2Node("f4", n);
        
        Node node2 = new Node("node2");
        node2.addRule("/a/b/*", "ANY", "g2");
        node2.addRule("/c/*", "ANY", "g3");
        node2.addRule("/y/c/*", "ANY", "g1");
        
        net.addFace2Node("g1", node2);
        net.addFace2Node("g2", node2);
        net.addFace2Node("g3", node2);

        net.addBiLink("f4", "g1");

        Node node3 = new Node("node3");
        node3.addRule("/a/b/*", "ANY", "g2");
        node3.addProviderName(new Name("/b/c/*"));
        
        net.addFace2Node("h1", node3);
        net.addFace2Node("h2", node3);

        net.addBiLink("g2", "h1");
        
        
        System.out.println("0:");
        net.printPacketLists();       

        int time = 1;
        
        while(net.getArrivingPackets().size()!=0){
            System.out.println(time+":");
            net.networkTransferUpdate();
            net.printPacketLists();
            time++;
            System.out.println(time+":");
            net.topologyTransferUpdates();
            net.printPacketLists();
            time++;
        }

        net.printProviderNames();
        
        net.printVisitedNames();
//        net.printArrivingVisitedNames();
//       net.printLeavingArrivingVisitedNames();
    }
}
