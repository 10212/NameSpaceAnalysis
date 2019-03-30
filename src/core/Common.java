/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;
import java.util.*;
/**
 *
 * @author MohammadHossein
 */
public class Common {

    public static int maxLength = 10;//maximum no of components in name
    public Name nameIntersection (Name name1, Name name2){
        Name result = new Name();
        int i=0;
        while(true){
            if(name1.getSize()<=i || name2.getSize()<=i){
                //System.out.println("component not extistant");
                break;
            }
            else if(name1.getComponentByIndex(i).equals(name2.getComponentByIndex(i))){
                result.addNameComponent(name1.getComponentByIndex(i));
            }          
            else if(name1.getComponentByIndex(i).equals("?")){
                result.addNameComponent(name2.getComponentByIndex(i));
            }
            else if(name2.getComponentByIndex(i).equals("?")){
                result.addNameComponent(name1.getComponentByIndex(i));
            }
            else if(name1.getComponentByIndex(i).equals("*")){
                for(int j=i; j< name2.getSize(); j++){
                    result.addNameComponent(name2.getComponentByIndex(j));
                }
                break;
            }
            else if(name2.getComponentByIndex(i).equals("*")){
                for(int j=i; j< name1.getSize(); j++){
                    result.addNameComponent(name1.getComponentByIndex(j));
                }
                break;
            }
            else if(name1.getComponentByIndex(i).charAt(0) == '!'){
                if(name2.getComponentByIndex(i).charAt(0) == '!'){
                    if(name1.getComponentByIndex(i).substring(1).equals(name2.getComponentByIndex(i).substring(1))){
                        result.addNameComponent(name1.getComponentByIndex(i));
                    }
                    else{
                        result.addNameComponent(name1.getComponentByIndex(i));
                        //result.addNameComponent(name1.getComponentByIndex(i)+name2.getComponentByIndex(i));
                        //System.out.println("Warning: "+name1.getComponentByIndex(i)+" "+name2.getComponentByIndex(i));
                    }
                }
                else if(name1.getComponentByIndex(i).substring(1).equals(name2.getComponentByIndex(i))){
                    Name result2 = new Name("/");
                    return result2;
                }
                else{
                    result.addNameComponent(name2.getComponentByIndex(i));
                }
            }

            else if(name2.getComponentByIndex(i).charAt(0) == '!'){
                if(name1.getComponentByIndex(i).charAt(0) == '!'){
                    if(name1.getComponentByIndex(i).substring(1).equals(name2.getComponentByIndex(i).substring(1))){
                        result.addNameComponent(name1.getComponentByIndex(i));
                    }
                    else{
                        result.addNameComponent(name1.getComponentByIndex(i));
                        //result.addNameComponent(name1.getComponentByIndex(i)+name2.getComponentByIndex(i));
                        //System.out.println("Warning: "+name1.getComponentByIndex(i)+" "+name2.getComponentByIndex(i));
                    }
                }
                else if(name2.getComponentByIndex(i).substring(1).equals(name1.getComponentByIndex(i))){
                    Name result2 = new Name("/");
                    return result2;
                }
                else{
                    result.addNameComponent(name1.getComponentByIndex(i));
                }
            }  
            else{
                Name result2 = new Name("/");
                return result2;
                //break;
            }
            i++;
        }        
        return result;
    }
    
    public SinglePacket singlePacketIntersection(SinglePacket p1, SinglePacket p2){
        SinglePacket result = new SinglePacket();
        if(p1.getFieldValues().equals(p2.getFieldValues())){
            result.setName(nameIntersection(p1.getName(), p2.getName()));
        }      
        return result;
    }
    

    public List<String> packetFaceList2String(List<PacketFace> pfList){
        List<String> result = new ArrayList<>();
        for(PacketFace pf: pfList){
            result.add(pf.packetFace2String());
        }
        return result;
    }
    
}
