package generator;
import core.Common;
import core.PacketFace;
import java.io.*;
import java.util.*;
/**
 *
 * @author MohammadHossein
 */
public class RuleGenerator {
    public static void main(String[] args) throws IOException {
        Common nsa = new Common();
        FileReader fr = new FileReader("nodesData/uci.txt_processed.txt");
        BufferedReader br = new BufferedReader(fr);        
        FileWriter fw = new FileWriter("fib-processed.txt");
        List<PacketFace> inList = new ArrayList<>();
        String line;//format: "prefix \t outFace);
        
        line = br.readLine();//first line is nodeName
        String nodeName = line;
        
        while((line = br.readLine())!=null){
            String [] elements = line.split("\t");
            String prefix = elements[0]+"/*";
            String outFace = elements[1];                                    
            inList.add(new PacketFace(prefix, outFace));
        }
        
        for(PacketFace pf1: inList){
            int hasSubset = 0;
            for(PacketFace pf2: inList){
                if(pf1==pf2){
                    continue;
                }
                if(pf1.getPacket().getNameAsString().equals(pf2.getPacket().getNameAsString())){
                    continue;
                }
                if(pf2.getPacket().getName().subsetOf(pf1.getPacket().getName())){
                    hasSubset=1;
                    int sizeDifference = pf2.getPacket().getName().getSize() - pf1.getPacket().getName().getSize();
                    System.out.println(pf1.getPacket().getName().name2String()+" MATCH "+pf2.getPacket().getName().name2String() + " SIZEDIFF: "+sizeDifference);
                    
                    //
                    for(int j =0; j< Math.pow(2, sizeDifference); j++){
                        String jBin = Integer.toBinaryString(j);
                        //padding 0s
                        int diff = sizeDifference - jBin.length();
                        for(int i=0; i< diff; i++){
                            jBin = "0"+ jBin;
                        }
                        if(!jBin.contains("1")){
                            continue;
                        }
                        System.out.println(jBin);
                        //0 same, 1 negative
                        System.out.print("-rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString().substring(0, pf1.getPacket().getNameAsString().length()-1));
                        for(int k=0; k<sizeDifference; k++){
                            if(jBin.charAt(k)=='0')
                                System.out.print(pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                            else
                                System.out.print("!"+pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                        }
                        System.out.print("*\tANY\t"+pf1.getFace()+"\n");
                        }
                }
            }
            if(hasSubset==0){
                System.out.println("--rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString()+"\tANY\t"+pf1.getFace());
            }
        }
        
        fw.close();
        br.close();
        fr.close();
        
    }
}
