package applications;

import core.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author MohammadHossein
 */
public class LoopDetection_Full { // for loop detection
        static int verbose=0;
        public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();    
        
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("file1b.txt");
        String fname = snapshot.getFileName();
        //remove added arriving packets
        net.setArrivingPackets(new ArrayList<PacketFace>());
        if(verbose==1){
            net.printNetworkSummary();
            System.out.println("---------------------------");
        }
        //now add inject one by one for each face
        Map <String, Node> face2NodeMap = net.getFace2Node();
        
        int instance =1;

        for(String injectFace: face2NodeMap.keySet()){
            if(verbose==1){
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
                net.topologyTransferUpdates(verbose);
                //net.printPacketLists();
                time++;
            }

            //net.printProviderNames();

            //net.printArrivingVisitedNames();

            //System.out.println("\nLoops: "+net.getLoops());
            
            instance++;
            //System.out.println("=====================================");
        }
        
    System.out.println((System.currentTimeMillis() - start)+" ms");        

    }
}
