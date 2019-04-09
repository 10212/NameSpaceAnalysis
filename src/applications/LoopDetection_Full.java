package applications;

import core.*;
import java.io.*;
import java.util.*;

/**
 * Application for loop detection; outputs all looped packets
 * 
 */
public class LoopDetection_Full { // for loop detection
        static int verbose=1;
        static int allFaces =0;
        public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();    
        
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("grid3.txt");
        String fname = snapshot.getFileName();
        //remove added arriving packets
        net.setArrivingPackets(new ArrayList<PacketFace>());
        if(verbose==1){
            net.printNetworkSummary();
            System.out.println("---------------------------");
        }
        //now add inject one by one for each face
        Map <String, Node> face2NodeMap = net.getFace2Node();
        Set <String> selectFaces;
        if(allFaces==1)
            //all faces
            selectFaces = face2NodeMap.keySet();
        else
        //or, get faces from file
            selectFaces = readFaces("faces.txt");
        
        int instance =1;

        for(String injectFace: selectFaces){
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
    
    public static Set <String> readFaces(String fname) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(fname);
        BufferedReader br = new BufferedReader(fr);
        Set<String> faces = new HashSet<>();
        String line;
        while((line=br.readLine())!=null){
            faces.add(line);
        }
        br.close();
        fr.close();
        return faces;
    }
}
