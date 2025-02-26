package core;

import java.io.*;
import java.util.*;
/**
 * @author Mohammad Jahanian
 * Reads snapshot from input file, parses and makes a network object out of it
 * 
 */
public class SnapshotReader {
    
    private Network net;
    private Map <String, Node> nodes;
    private String fileName;
    
    public SnapshotReader(){
        net = new Network();
        nodes = new HashMap<>();
        fileName="";
    }

    public String getFileName() {
        return fileName;
    }
    
    
    public Network readSnapshotFromFile(String fileName) throws FileNotFoundException, IOException{
        this.fileName = fileName;
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        String line = br.readLine(); // skip first header line
        
        while(line!=null){
            String [] elements = line.split("\t");
            if(elements[0].equals("-node")){
                String nodeName = elements[1];
                Node node;
                if(!nodes.containsKey(nodeName)){
                    node = new Node(nodeName);
                    nodes.put(nodeName, node);
                }
                else{
                    node = nodes.get(nodeName);
                }
                for(int i=2; i<elements.length; i++){
                    net.addFace2Node(elements[i], node);
                }
            }
            
            else if(elements[0].equals("-link")){
                net.addBiLink(elements[1], elements[2]);
                if(!net.getFace2Node().containsKey(elements[1])){
                    System.out.println("XXX face "+elements[1]+" does not exist!");
                }
                if(!net.getFace2Node().containsKey(elements[2])){
                    System.out.println("XXX face "+elements[2]+" does not exist!");
                }
            }
            
            else if(elements[0].equals("-rule")){
                String nodeName = elements[1];
                Node node = nodes.get(nodeName);
                node.addRule(elements[2], elements[3], elements[4]);
                if(!net.getFace2Node().containsKey(elements[4])){
                    System.out.println("XXX face "+elements[4]+" does not exist!");
                }
            }
            
            else if(elements[0].equals("-provider")){
                String nodeName = elements[1];
                Node node = nodes.get(nodeName);
                Name name = new Name(elements[2]);
                node.addProviderName(name);
            }

            else if(elements[0].equals("-prohibited")){
                String nodeName = elements[1];
                Node node = nodes.get(nodeName);
                Name name = new Name(elements[2]);
                node.addProhibitedNames(name);
            }
            
            else if(elements[0].equals("-inject")){
                PacketFace pf = new PacketFace(elements[1], elements[2]);
                net.addArrivingPackets(pf);
            }
            
            else if(elements[0].equals("-injectAll")){
                for(String f: net.getFace2Node().keySet()){
                    net.addArrivingPackets(new PacketFace(elements[1], f));
                }
            }
            
            
            
            line = br.readLine();
            //System.out.println(line);
        }
        
        br.close();
        fr.close();
        return net;
    }
    
}
