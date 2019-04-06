package applications;
import java.io.*;
import java.util.*;
import core.*;
/**
 * Name leakage detection application; outputs all name leakage violations
 * @author Mohammad Jahanian
 */
public class NameLeakage {
    static int verbose=1;
    static int allFaces=1;
    public static void main(String [] args) throws IOException{
        long start = System.currentTimeMillis();
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("ring2x2.txt");
        String fname = snapshot.getFileName();
        //remove added arriving packets
        net.setArrivingPackets(new ArrayList<PacketFace>());
        if(verbose==1){
            net.printNetworkSummary();
            System.out.println("===================================================");
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
        Map<String, List<String>> zones = readZones("zones.txt");
        for(String zoneID: zones.keySet()){
            List<String> zoneNodes = zones.get(zoneID);
            if(verbose==1){
                System.out.println("------------------------------------------------");
                System.out.println(instance+") INJECT from "+zoneID+":"+zoneNodes + " *******************************");
            }
            snapshot = new SnapshotReader();
            net = snapshot.readSnapshotFromFile(fname);
            //remove added arriving packets
            net.setArrivingPackets(new ArrayList<PacketFace>());
            for(String face: selectFaces){
                String nodeID = face2NodeMap.get(face).getNodeID();
                if(zoneNodes.contains(nodeID)){
                    net.addArrivingPackets(new PacketFace("/*", face));
                }
            }
            net.printArrivingPacketLists();
            int time = 1;
        
            while(net.getArrivingPackets().size()!=0){
                if(verbose==1)
                    System.out.println(time+":");
                net.networkTransferUpdate();
                if(verbose==1)
                    net.printPacketLists();
                time++;
                if(verbose==1)
                    System.out.println(time+":");
                net.topologyTransferUpdates(0);
                if(verbose==1)
                    net.printPacketLists();
                time++;
            }
            if(verbose==1){
                net.printProhibitedNames();
                net.printArrivingVisitedNames();
            }
            //comparison 
            if(verbose==1)
                System.out.println("\n*****Comparison: ProhibitedNames < ArrivingVisitedNames? (all false is desirable)");
            Set <String> nodeNames = new HashSet<>();
            for(Node n: net.getFace2Node().values()){
                //ignore if node nothing prohibited or from zone
                if(n.getProhibitedNames().isEmpty() || zoneNodes.contains(n.getNodeID())){
                    continue;
                }
                if(!nodeNames.contains(n.getNodeID())){ 
                   if(verbose==1)
                        System.out.println(n.getNodeID()+" : ");
                    List <Name> prohibitedNames = n.getProhibitedNames();
                    //n.printArrivingVisitedNames();
                    Set <String> arrivingVisitedNames = n.getArrivingVisitedNames();
                    nodeNames.add(n.getNodeID());
                    //compare
                    for(Name pn: prohibitedNames){
                        boolean check=false; // true is it is subset (bad)
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
        
        instance++;
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
    
    public static Map <String, List<String>> readZones(String fname) throws FileNotFoundException, IOException{
        //map zone ID to nodes
        FileReader fr = new FileReader(fname);
        BufferedReader br = new BufferedReader(fr);
        Map<String, List<String>> zones = new HashMap<>();
        String line;
        while((line=br.readLine())!=null){
            String [] elements = line.split("\t");
            List <String> zoneFaces = new ArrayList<>();
            for(int i=1; i< elements.length; i++){
                zoneFaces.add(elements[i]);
            }
            zones.put(elements[0], zoneFaces);
        }
        br.close();
        fr.close();
        return zones;
    }
}


