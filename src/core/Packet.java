
package core;
import java.util.*;

/**
 * NDN packet
 * 
 */
public class Packet {

    private Name name;
    private Map <String, String> fieldValues;
    private int TTL;
    private List<String> nodesNamesVisited;
    
    public Packet(){
        name = new Name();
        fieldValues = new HashMap<>();
    }
    
    public Packet(Name name, Map <String, String> fields){
        this.name = name;
        fieldValues = fields;
    }

    public Packet(String name, Map <String, String> fields){
        this.name = new Name(name);
        fieldValues = fields;
    }

    public Packet(Name name){
        this.name = name;
        fieldValues = new HashMap<>();
        
    }

    public Packet(String name){
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
