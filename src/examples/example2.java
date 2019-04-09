package examples;
import core.Network;
import core.SnapshotReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * 
 */
public class example2 {
    
    public static void main(String[] args) throws IOException {
        
        SnapshotReader snapshot = new SnapshotReader();
        Network net = snapshot.readSnapshotFromFile("file2b.txt");
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

        
        
    }
    
}
