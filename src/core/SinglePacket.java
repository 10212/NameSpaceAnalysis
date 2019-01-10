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
public class SinglePacket {

    private Name name;
    private Map <String, String> fieldValues;
    private int TTL;
    private List<String> nodesNamesVisited;
    
    public SinglePacket(){
        name = new Name();
        fieldValues = new HashMap<>();
    }
    
    public SinglePacket(Name name, Map <String, String> fields){
        this.name = name;
        fieldValues = fields;
    }

    public SinglePacket(String name, Map <String, String> fields){
        this.name = new Name(name);
        fieldValues = fields;
    }

    public SinglePacket(Name name){
        this.name = name;
        fieldValues = new HashMap<>();
        
    }

    public SinglePacket(String name){
        this.name = new Name(name);
        fieldValues = new HashMap<>();
    }
    
    public Name getName(){
        return name;
    }
    
    public List<String> getNameComponentList(){
        return name.getName();     
    }
    
    public String getNameAsString(){
        return name.name2String();
    }
    
    public Map <String, String> getFieldValues(){
        return fieldValues;
    }        
    
    public void setName(Name name){
        this.name = name;
    }    
}
