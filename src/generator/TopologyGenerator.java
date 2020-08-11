/*

 */
package generator;
import core.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Mohammad Jahanian
 * create custom size grid topology snapshots
 */
public class TopologyGenerator {
    static int grid_dim = 10; // dimesntions, e.g. 5 is 5x5, with 5 producer; adjust
    static boolean oneFace = false; //true:inject one face; false: all faces; adjust
    static int pro_prefixes = 1; // prefix per provider; adjust
    static int grid_size = grid_dim * grid_dim;
    static public Set <String> nodes;
    static public Map <String, String> node2oneFace;
    static public Map <String, List<String>> node2allFaces = new HashMap<>();
    static public Set <String> face2face; // topology
    static public Map <String, List<String>> pro2prefixes; // producer 2 prefixes
    static public Set <String> allPrefixes; // all prefixes across all providers

    public static void main(String[] args) throws IOException {
        nodes = new HashSet<>();
        node2oneFace = new HashMap<>();
        node2allFaces = new HashMap<>();
        face2face = new HashSet<>();
        pro2prefixes = new HashMap<>();
        allPrefixes = new HashSet<>();
        
        for(int i=0; i<grid_size; i++){
            if(i%grid_dim!=grid_dim-1){
                nodes.add("n"+i);
            }
            else{
                nodes.add("p"+i);
                //provider prefixes
                List <String> prefixes = new ArrayList<>();
                for(int j=0; j<pro_prefixes; j++){
                    String newPrefix = "/p"+i+"_"+((char)(j+97))+"/*";
                    prefixes.add(newPrefix);
                    allPrefixes.add(newPrefix);
                }
                pro2prefixes.put("p"+i, prefixes);
            }
        }
        
        for(String nodeName: nodes){
            node2allFaces.put(nodeName, new ArrayList<>());
        }

        String newFace;
        for(int i=0; i<grid_size; i++){
            if(i%grid_dim!=grid_dim-1) { // add East
                newFace = addFace2Node(i, "E");
                face2face.add(newFace+"\t"+faceString(i+1, "W"));
            }           
            if(i%grid_dim!=0) { // add West
                newFace = addFace2Node(i, "W");
            } 
            if(i>=grid_dim) { // add North
                newFace = addFace2Node(i, "N");
                face2face.add(newFace+"\t"+faceString(i-grid_dim, "S"));
            }
            if(i<((grid_dim-1)*grid_dim)) { // add South
                newFace = addFace2Node(i, "S");
            }

        }
    
    
    System.out.println("\nnodes:\n"+nodes);
    System.out.println("\nnode2allFaces:\n"+node2allFaces);
    System.out.println("\nface2face:\n"+face2face);
    System.out.println("\npro2prefixes:\n"+pro2prefixes);
    System.out.println("\nallPrefixes:\n"+allPrefixes);
    
    print2file();
    }
    
    public static String addFace2Node(int nodeID, String faceType){
        String node = nodeString(nodeID);
        String face = faceString(nodeID, faceType);
        List <String> faces = node2allFaces.get(node);
        faces.add(face);
        node2allFaces.put(node, faces);
        //also add to oneFace
        node2oneFace.put(node, face);
        return face;
    }
    
    public static String nodeString(int nodeID){
        String node;
        if(nodeID%grid_dim!=grid_dim-1){
            node = "n"+nodeID;
        }
        else{
            node = "p"+nodeID;
        }
        return node;
    }
    
    public static String faceString(int nodeID, String faceType){
        String face;
        String node;
        if(nodeID%grid_dim!=grid_dim-1){
            node = "n"+nodeID;
        }
        else{
            node = "p"+nodeID;
        }
        face = node+"."+faceType;
        return face;
    }
    
    public static void print2file() throws IOException{
        FileWriter fw = new FileWriter("grid"+grid_dim+"_"+pro_prefixes+"p_"+ (oneFace?"one":"all")+ ".txt");
        BufferedWriter bw = new BufferedWriter(fw);
        //nodes
        for(String node: node2allFaces.keySet()){
            List<String> nodeFaces = node2allFaces.get(node);
            bw.write("-node\t"+node);
            for(String face: nodeFaces){
                bw.write("\t"+face);
            }
            bw.write("\n");
        }
        
        //rules
        bw.write("#rules\n");
        for(String node: node2allFaces.keySet()){
            for(String prefix: allPrefixes){
                bw.write("-rule\t"+node+"\t"+prefix+"\tANY\t"+getRandFace(node)+"\n");
            }
        }
        
        //links
        bw.write("#links\n");
        for(String link: face2face){
            bw.write("-link\t"+link+"\n");
        }
        
        //providers
        bw.write("#provider\n");
        for(String provider: pro2prefixes.keySet()){
            for(String prefix: pro2prefixes.get(provider)){
                bw.write("-provider\t"+provider+"\t"+prefix+"\n");
            }
        }
        
        //injects
        bw.write("#injects\n");
        if(oneFace==true){//one face-injection per node
            for(String node: node2oneFace.keySet()){
                bw.write("-inject\t/*\t"+node2oneFace.get(node)+"\n");
            }
        }
        else{//inject from all faces of nodes
            bw.write("-injectAll\t/*\n");
        }
        
        
        bw.close();
        fw.close();
    }
    
    public static String getRandFace(String node){
        List <String> faces = node2allFaces.get(node);
        Random randomNum = new Random();
        int rand = randomNum.nextInt(faces.size());
        return faces.get(rand);
    }
}
