package applications;

import core.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author MohammadHossein
 */
public class Reachability_Full { // reachability check: inject /* one by one
        static int verbose=0;
        public static void main(String[] args) throws IOException {
        
        long start = System.currentTimeMillis();
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("file2b.txt");
        String fname = snapshot.getFileName();
        //remove added arriving packets
        net.setArrivingPackets(new ArrayList<PacketFace>());
        if(verbose==1){
            net.printNetworkSummary();
            System.out.println("===================================================");
        }
        //now add inject one by one for each face
        Map <String, Node> face2NodeMap = net.getFace2Node();
        
        int instance =1;

        for(String injectFace: face2NodeMap.keySet()){
            if(verbose==1){
                System.out.println("------------------------------------------------");
                System.out.println(instance+") INJECT from "+injectFace+" @ " + face2NodeMap.get(injectFace).getNodeID()+ " *******************************");
                System.out.println(face2NodeMap.get(injectFace).getNodeID());
            }
            snapshot = new SnapshotReader();
            net = snapshot.readSnapshotFromFile(fname);
            //remove added arriving packets
            net.setArrivingPackets(new ArrayList<PacketFace>());
            
            //add one inject Face
            net.addArrivingPackets(new PacketFace("/*", injectFace));
            //net.printNetworkSummary();

            //System.out.println("---------------------------------------");

            //System.out.println("0:");
            //net.printPacketLists();       

            int time = 1;

            while(net.getArrivingPackets().size()!=0){
                //System.out.println(time+":");
                net.networkTransferUpdate();
                //net.printPacketLists();
                time++;
                //System.out.println(time+":");
                net.topologyTransferUpdates(0);
                //net.printPacketLists();
                time++;
            }
            
            if(verbose==1)
                net.printProviderNames();
            if(verbose==1)
                net.printArrivingVisitedNames();

            //System.out.println("\nLoops: "+net.getLoops());
            
            instance++;
            //System.out.println("=====================================");
            if(verbose==1)
                System.out.println("\n**********\nCompare ProviderNames and ArrivingVisitedNames:");
            //comparison 1
            if(verbose==1)
                System.out.println("\n*****Comparison1: ProvdiderNames < ArrivingVisitedNames? (lack of full ns coverage)");
            Set <String> nodeNames = new HashSet<>();
            for(Node n: net.getFace2Node().values()){
                //ignore if node not provider
                if(n.getProviderNames().isEmpty()){
                    continue;
                }
               if(!nodeNames.contains(n.getNodeID())){ 
                   if(verbose==1)
                        System.out.println(n.getNodeID()+" : ");
                    //n.printProviderNames();
                    List <Name> providerNames = n.getProviderNames();
                    //n.printArrivingVisitedNames();
                    Set <String> arrivingVisitedNames = n.getArrivingVisitedNames();
                    nodeNames.add(n.getNodeID());
                    //compare
                    for(Name pn: providerNames){
                        boolean check=false; // true is it is subset
                        for(String avn: arrivingVisitedNames){
                            Name avn_name = new Name(avn);
                            if(verbose==1)
                                System.out.println(pn.name2String()+" < "+avn+" ? "+ pn.subsetOf(avn_name));
                            if(pn.subsetOf(avn_name)){
                                check=true;
                                //break;
                            }
                        }
                        if(verbose==1)
                            System.out.print("\t"+pn.name2String()+ " < allArrivingVisited ? "+ check+"\n");
                    }
                    if(verbose==1)
                        System.out.println();
               }
            }
            //comparison 2
            if(verbose==1)
                System.out.println("\n*****Comparison2: ArrivingVisitedNames < ProvdiderNames? (lack of blackholed packets)");
            nodeNames = new HashSet<>();
            for(Node n: net.getFace2Node().values()){
                //ignore if node not provider
                if(n.getProviderNames().isEmpty()){
                    continue;
                }
               if(!nodeNames.contains(n.getNodeID())){ 
                    if(verbose==1)
                        System.out.println(n.getNodeID()+" : ");
                    //n.printProviderNames();
                    List <Name> providerNames = n.getProviderNames();
                    //n.printArrivingVisitedNames();
                    Set <String> arrivingVisitedNames = n.getArrivingVisitedNames();
                    nodeNames.add(n.getNodeID());
                    //compare
                    for(String avn: arrivingVisitedNames  ){
                        Name avn_name = new Name(avn);
                        boolean check=false; // true is it is subset
                        for(Name pn: providerNames){                        
                            if(verbose==1)
                                System.out.println(avn+" < "+pn.name2String()+" ? "+ avn_name.subsetOf(pn));
                            if(avn_name.subsetOf(pn)){
                                check=true;
                                //break;
                            }
                        }
                        if(verbose==1)
                            System.out.print("\t"+avn+ " < allProviderNames ? "+ check+"\n");
                    }
                    if(verbose==1)
                        System.out.println();
               }
            }

        }
        System.out.println((System.currentTimeMillis() - start)+" ms");        
        
    }
}
