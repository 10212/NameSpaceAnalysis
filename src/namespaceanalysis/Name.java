/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namespaceanalysis;
import java.util.*;
/**
 *
 * @author MohammadHossein
 */
public class Name {
    private List <String> nameComponents;
    // string, * (0 or more wildcard), ? (0 or 1 wildcard). !... (negation), "/" means empty name
    private int nameSize;
    
    public Name(List <String> components){
        nameComponents = components;
        nameSize = nameComponents.size();
    }
    
    public Name(){
        nameComponents = new ArrayList<>();
        nameSize=0;
    }
    
    public Name(String name){
        //format: /a/b/* ...
        name = name.substring(1, name.length());
        String [] components = name.split("/");
        nameComponents = new ArrayList<>();
        for (String component: components){
            nameComponents.add(component);
        }
        nameSize = nameComponents.size();
    }
    
    public List <String> getName(){
        return nameComponents;
    }
    
    public void setName(List <String> name){
        nameComponents= name;
        nameSize = name.size();
    }
    
    public void setSize(int size){
        nameSize = size;
    }
    
    public int getSize(){
        return nameSize;
    }
    
    
    public String name2String(){
        String name = "";
        for(String s: nameComponents){
            name+="/"+s;
        }
        return name;
    }
    
    public String getComponentByIndex(int index){
        if(index>=nameComponents.size()){
            return "notExist";
        }
        return nameComponents.get(index);
    }
    
    public void addNameComponent (String component){
        nameComponents.add(component);
        nameSize++;
    }
    
    public boolean subsetOf(Name name){
        //returns true if this is a subset of name
        int i=0;
        String nameS = "";
        for(i=0; i<this.nameSize; i++){ 
            String thisS = this.getComponentByIndex(i);
            if(i<name.nameSize){
                nameS = name.getComponentByIndex(i);
            }
            else{
                return false;
            }
            if(nameS.equals(thisS) || nameS.equals("?")){
                continue;
            }
            else if(nameS.equals("*")){
                return true;
            }
            else{
                return false;
            }
        }
        
        return true;
    }
}
