package generator;

import java.io.*;
import java.util.*;

/**
 *
 * @author MohammadHossein
 */
public class preProcessFIB {
    public static void main(String[] args) throws IOException {
        BufferedReader logReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter input file path: "); 
        String infname = logReader.readLine();
        File directory = new File(infname);
        File[] listOfFiles = directory.listFiles();        
        for (File file : listOfFiles) {
            if(file.getName().contains("_processed")){
                continue;
            }
            String fname = file.getName();
            processInputFIB(infname+"//"+fname, "multicast", fname);
            //processFaces(infname+"//"+fname, fname);
        }        
    }
    
    public static void processInputFIB(String fname, String strategy, String justFname) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(fname);
        //fname is Node ID minus extension
        String nodeID = justFname.replace(".txt", "");
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(fname+"_processed.txt");
        fw.write(nodeID+"\n");
        
        String line;
        
        while((line=br.readLine())!=null){
            //first line is prefix
            String prefix = line;
            prefix = prefix.replace(" ", "");
            prefix = prefix.replace("/t", "");
            //2nd line is face list
            line = br.readLine();
            String [] faceElements = line.split("\t");
            //first element is 'FaceId'
            //only for multicast
            if(strategy.equals("multicast")){
                for(int i=1; i<faceElements.length; i++){
                    faceElements [i] = faceElements [i].replace(" ", "");
                    fw.write(prefix+nodeID+faceElements[i]+"\n");
                }
            }
            //for best route; pick lowest cost
            //3rd line is cost list
            line = br.readLine();
            String [] costElements = line.split("\t");
            //first element is 'Cost'
            int minCost = Integer.parseInt(costElements[1]);
            int minCostIndex=1;
            for(int i=1; i<costElements.length; i++){
                if(Integer.parseInt(costElements[i])<minCost){
                    minCost = Integer.parseInt(costElements[i]);
                    minCostIndex = i;
                }
            }
            //only for best route
            if(strategy.equals("best-route")){
                faceElements[minCostIndex] = faceElements[minCostIndex].replace(" ", "");
                fw.write(prefix+nodeID+faceElements[minCostIndex]+"\n");
            }
        }
        
        fw.close();
        fr.close();
        br.close();
    }
    
    public static void processFaces(String fname, String justFname) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(fname);
        FileWriter fw = new FileWriter(fname+"_processed.txt");
        //fname is Node ID minus extension
        String nodeID = justFname.replace("_faces.txt", "");
        BufferedReader br = new BufferedReader(fr);
        fw.write("-node\t"+nodeID);
        String line;
        
        while((line=br.readLine())!=null){
            String [] elements = line.split("\t");
            //only take non-local faces
            if (elements[3].equals("non-local")){
                fw.write("\t"+nodeID+elements[0]);
            }           
        }
        fw.write("\n");
        
        fw.close();
        br.close();
        fr.close();
    }
}
