package generator;
import core.Common;
import core.PacketFace;
import java.io.*;
import java.util.*;
/**
 * Generate NSA rule (network transfer function) from FIB rules 
 * @author Mohammad Jahanian
 */
public class RuleGenerator {
    
    static int verbose=1;
    
    public void generateNSArules(String fname) throws IOException {
        //long start = System.currentTimeMillis();    
        Common nsa = new Common();
        FileReader fr = new FileReader(fname);
        BufferedReader br = new BufferedReader(fr);        
        FileWriter fw = new FileWriter(fname+"_fib-processed.txt");
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
//                    System.out.println(pf1.getPacket().getName().name2String()+" MATCH "+pf2.getPacket().getName().name2String() + " SIZEDIFF: "+sizeDifference);
//                    if(verbose==1)
//                        fw.write(pf1.getPacket().getName().name2String()+" MATCH "+pf2.getPacket().getName().name2String() + " SIZEDIFF: "+sizeDifference+"\n");
                    
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
//                        System.out.println(jBin);
//                        if(verbose==1)
//                            fw.write(jBin+"\n");

                        //0 same, 1 negative
//                        System.out.print("-rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString().substring(0, pf1.getPacket().getNameAsString().length()-1));
                        if(verbose==1)
                            fw.write("-rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString().substring(0, pf1.getPacket().getNameAsString().length()-1));

                        for(int k=0; k<sizeDifference; k++){
                            if(jBin.charAt(k)=='0'){
//                                System.out.print(pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                                if(verbose==1)
                                    fw.write(pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                            }
                            else{
//                                System.out.print("!"+pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                                if(verbose==1)
                                    fw.write("!"+pf2.getPacket().getName().getComponentByIndex(k+ pf1.getPacket().getName().getSize()-1)+"/");
                            }
                        }
//                        System.out.print("*\tANY\t"+pf1.getFace()+"\n");
                        if(verbose==1)
                            fw.write("*\tANY\t"+pf1.getFace()+"\n");

                        }
                }
            }
            if(hasSubset==0){
//                System.out.println("--rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString()+"\tANY\t"+pf1.getFace());
                if(verbose==1)
                    fw.write("-rule\t"+nodeName+"\t"+pf1.getPacket().getNameAsString()+"\tANY\t"+pf1.getFace()+"\n");

            }
        }

        
        fw.close();
        br.close();
        fr.close();
        //System.out.println((System.currentTimeMillis() - start)+" ms");        
        
    }
}
