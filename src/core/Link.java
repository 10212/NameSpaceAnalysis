
package core;

/**
 * link information for topology function
 * 
 */
public class Link {
    private String face1, face2;

    public Link(String face1, String face2) {
        this.face1 = face1;
        this.face2 = face2;
    }

    
    public String getFace1() {
        return face1;
    }

    public void setFace1(String face1) {
        this.face1 = face1;
    }

    public String getFace2() {
        return face2;
    }

    public void setFace2(String face2) {
        this.face2 = face2;
    }
    
    
}
