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
public class Node {
    private List<Rule> rules;
    private String nodeID;
    private List<Name> providerNames;
    private Set<String> visitedNames;
    private Set<String> arrivingVisitedNames;
    private Set<String> leavingVisitedNames;
   

    public Node(String nodeID) {
        this.rules = rules = new ArrayList<>();
        this.nodeID = nodeID;
        this.providerNames = new ArrayList<>();
        this.visitedNames = new HashSet<>();
        this.arrivingVisitedNames = new HashSet<>();
        this.leavingVisitedNames = new HashSet<>();
        
    }
        
    public Node(List<Rule> rules, String nodeID) {
        this.rules = rules;
        this.nodeID = nodeID;
        this.providerNames = new ArrayList<>();
        this.visitedNames = new HashSet<>();
        this.arrivingVisitedNames = new HashSet<>();
        this.leavingVisitedNames = new HashSet<>();

    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
    
    public void addRule(String rule, String face_in, String face_out){
        Rule rule2add = new Rule(rule, face_in, face_out);
        rules.add(rule2add);
    }
    
    public void addRule(Rule rule){
        rules.add(rule);
    }    

    public List<Name> getProviderNames() {
        return providerNames;
    }

    public void setProviderNames(List<Name> providerNames) {
        this.providerNames = providerNames;
    }
    
    public void addProviderName(Name name){
        providerNames.add(name);
    }
        
    public List<String> providerNames2stringList(){
        List<String> results = new ArrayList<>();
        for(Name n: providerNames){
            results.add(n.name2String());
        }
        return results;
    }

    public void printProviderNames(){
        System.out.print(nodeID+": ");
        for(Name n: providerNames){
            System.out.print(n.name2String()+" ");
        }
    }    
    
    public void printVisitedNames(){
        System.out.print(nodeID+": ");
        for(String s: visitedNames){
            System.out.print(s+" ");
        }
    } 
    
    public void printArrivingVisitedNames(){
        System.out.print(nodeID+": ");
        for(String s: arrivingVisitedNames){
            System.out.print(s+" ");
        }
    } 
    
    public void printLeavingVisitedNames(){
        System.out.print(nodeID+": ");
        for(String s: leavingVisitedNames){
            System.out.print(s+" ");
        }
    } 
    
    public List<PacketFace> nodeTransfer(PacketFace packetFace_in){// network transfer function
        List <PacketFace> packetFaces_out = new ArrayList<>();
        for(Rule r: rules){
            if(r.matchInFace(packetFace_in)){
                PacketFace packetFace_out1 = r.matchRule(packetFace_in);
                packetFaces_out.add(packetFace_out1);
            }
        }
        return packetFaces_out;
    }

    public Set<String> getVisitedNames() {
        return visitedNames;
    }

    public void setVisitedNames(Set<String> visitedNames) {
        this.visitedNames = visitedNames;
    }
    
    public void addVisitedName(String name){
        visitedNames.add(name);
    }

    public Set<String> getArrivingVisitedNames() {
        return arrivingVisitedNames;
    }

    public void setArrivingVisitedNames(Set<String> visitedNames) {
        this.arrivingVisitedNames = visitedNames;
    }
    
    public void addArrivingVisitedName(String name){
        arrivingVisitedNames.add(name);
    }    
    
}
