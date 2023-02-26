
package project;


public class Node {
    private String name;
    private Node parent;
    private Node[] children;
    private String[] gameText;
    
    public Node(String name, String[] gameText) {
        this.name = name;
        this.gameText = gameText;
        this.children = new Node[0];
        
    }
    
    public String getName() {
        return this.name;
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public Node[] getChildren() {
        return this.children;
    }
    
    public void setChildren(Node[] children) {
        this.children = children;
    }
    
    public String[] getGameText() {
        return this.gameText;
    }
    
    public Node[] getConnections() {
        Node[] connections = new Node[children.length + 1];
        for(int i = 0; i < children.length; i++) {
            connections[i] = children[i];
        }
        connections[children.length] = parent;
        
        return connections;
    }
 
}
