
package project;

import java.util.ArrayList;


public class RoomModel {
    private Node position;
    private String[] textAndOptions;
    private Node startNode;
    private GameText gt = new GameText();
    private Node[] allNodes;
    
    public RoomModel(String[] textAndOptions) {
        this.textAndOptions = textAndOptions;
        createRoom();
    }
    
    public Node getPosition() {
        return this.position;
    }
    
    public void setPosition(Node newPosition) {
        this.position = newPosition;
    }
    
    public String getOptions() {
        return this.textAndOptions[1];
    }
    
    public String getText() {
        return this.textAndOptions[0];
    }
    
    public Node[] getAllNodes() {
        return this.allNodes;
    }
    
    public void setStart() {
        this.position = startNode;
        this.textAndOptions = gt.start();
    }
    
    public void setTextAndOptions(String[] newTextAndOptions) {
        this.textAndOptions = newTextAndOptions;
    }
    
    //OPTIONS NUMBERS MUST BE UNDER 10!
    public Boolean verifyOption(String option) {
        String optionsNumbers= textAndOptions[1].replaceAll("[^0-9]", "");
        for(int i = 0; i < optionsNumbers.length(); i++) {
            if(Character.toString(optionsNumbers.charAt(i)).equals(option)) {
                return true;
            }
        }
        return false;
    }
    
    /*public String gameText(){
    
    }*/    
    
    public void createRoom() {
        /*Create all nodes*/
        Node start = new Node("start", gt.start());
        Node north = new Node("north", gt.north());
        Node west = new Node("west", gt.west());
        Node east = new Node("east", gt.east());
        Node south = new Node("south", gt.south());
        Node coffee = new Node("coffee", gt.coffee());
        Node painting = new Node("painting", gt.painting());
        Node bookshelf = new Node("bookshelf", gt.bookshelf());
        Node book = new Node("book", gt.book());
        Node window = new Node("window", gt.window());
        Node couch = new Node("couch", gt.couch());
        Node desk = new Node("desk", gt.desk());
        
        Node pencil = new Node("pencil", gt.pencil());
        Node stapler = new Node("stapler", gt.stapler());
        Node phone = new Node("phone", gt.phone());
        Node desklamp = new Node("desklamp", gt.desklamp());
        Node notepad = new Node("notepad", gt.notepad());
        
        Node[] allNodes = {start,north,west,east,south,coffee,painting,bookshelf,book,window,couch,desk,pencil,stapler,phone,desklamp,notepad};
        this.allNodes = allNodes;
        
        /*Set children*/
        Node[] startChildren = {north,west,east,south};
        start.setChildren(startChildren);
        Node[] northChildren = {coffee,painting};
        north.setChildren(northChildren);
        Node[] westChildren = {bookshelf,window};
        west.setChildren(westChildren);
        Node[] eastChildren = {couch};
        east.setChildren(eastChildren);
        Node[] southChildren = {desk};
        south.setChildren(southChildren);
        Node[] deskChildren = {pencil,stapler,phone,desklamp,notepad};
        desk.setChildren(deskChildren);
        Node[] bookshelfChildren = {book};
        bookshelf.setChildren(bookshelfChildren);
        
        /*Set parent*/
        north.setParent(start);
        west.setParent(start);
        east.setParent(start);
        south.setParent(start);
        coffee.setParent(north);
        painting.setParent(north);
        bookshelf.setParent(west);
        book.setParent(bookshelf);
        window.setParent(west);
        couch.setParent(east);
        desk.setParent(south);
        
        pencil.setParent(desk);
        phone.setParent(desk);
        stapler.setParent(desk);
        desklamp.setParent(desk);
        notepad.setParent(desk);
        
        this.position = start;
        this.startNode = start;

    }
    
}
