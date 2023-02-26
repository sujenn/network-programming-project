
package project;

public class DTOBook {
    private String title;
    private String description;
    
    public DTOBook(){}
    
    public DTOBook(String title, String desc){
        this.title = title;
        this.description = desc;
    }
    
    public String getTitle(){ return this.title; }
    public String getDescription(){ return this.description; }
    
}
