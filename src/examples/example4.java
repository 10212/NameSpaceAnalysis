package examples;
import core.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author MohammadHossein
 */
public class example4 { // content reachability analysis; all at once
    
    public static void main(String[] args) throws IOException {
        
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("file2a.txt");
        net.printNetworkSummary();
        
        System.out.println("---------------------------------------");
        
        System.out.println("0:");
        net.printPacketLists();       

        int time = 1;
        
        while(net.getArrivingPackets().size()!=0){
            System.out.println(time+":");
            net.networkTransferUpdate();
            net.printPacketLists();
            time++;
            System.out.println(time+":");
            net.topologyTransferUpdates(0);
            net.printPacketLists();
            time++;
        }

        net.printProviderNames();
        
//        net.printVisitedNames();
        net.printArrivingVisitedNames();
//       net.printLeavingArrivingVisitedNames();

        //System.out.println("\nLoops: "+net.getLoops());

        //Check compare betweeb ProviderNames and ArrivingVisitedNames
        System.out.println("\n--------------------------------------------------------\nCompare ProviderNames and ArrivingVisitedNames:");

        //comparison 1
        System.out.println("\n*****Comparison1: ProvdiderNames < ArrivingVisitedNames? (lack of full ns coverage)");
        Set <String> nodeNames = new HashSet<>();
        for(Node n: net.getFace2Node().values()){
            //ignore if node not provider
            if(n.getProviderNames().isEmpty()){
                continue;
            }
           if(!nodeNames.contains(n.getNodeID())){ 
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
                        System.out.println(pn.name2String()+" < "+avn+" ? "+ pn.subsetOf(avn_name));
                        if(pn.subsetOf(avn_name)){
                            check=true;
                            //break;
                        }
                    }
                    System.out.print("\t"+pn.name2String()+ " < allArrivingVisited ? "+ check+"\n");
                }
                System.out.println();
           }
        }
        //comparison 2
        System.out.println("\n*****Comparison2: ArrivingVisitedNames < ProvdiderNames? (lack of blackholed packets)");
        nodeNames = new HashSet<>();
        for(Node n: net.getFace2Node().values()){
            //ignore if node not provider
            if(n.getProviderNames().isEmpty()){
                continue;
            }
           if(!nodeNames.contains(n.getNodeID())){ 
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
                        System.out.println(avn+" < "+pn.name2String()+" ? "+ avn_name.subsetOf(pn));
                        if(avn_name.subsetOf(pn)){
                            check=true;
                            //break;
                        }
                    }
                    System.out.print("\t"+avn+ " < allProviderNames ? "+ check+"\n");
                }
                System.out.println();
           }
        }
    }
    
}
