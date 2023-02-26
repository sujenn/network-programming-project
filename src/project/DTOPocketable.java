
package project;

/**
 * A representation of an object a player can put in his pocket.
 * These objects should be in databse by default
    ('Filled coffee mug', 'Lukewarm coffee. You have managed to keep it from spilling')
    ('Empty coffee mug','A single use paper coffee mug');
    ('Paperclip', 'Made of copper, and it is winking at you');
    ('Hairtie','Surely you can find a use for this');
    ('Small coin', 'Mmm! Tasty gold!');
    ('Empty wrapper', 'Probably the wrapper of a stick of gum');
 * 
 * An object is created by taking the name and description from table POCKETABLE 
 * and the quantity from PLAYERPOCKETABLE
 * 
 * The contents of a player's pocket is represented by a List of DTOPocketables
 */
public class DTOPocketable {
    private String name;
    private String description;
    private Integer quantity;
    
    public DTOPocketable(){}
    
    public DTOPocketable(String name, String desc, Integer quantity){
        this.name = name;
        this.description = desc;
        this.quantity = quantity;
    }
    
    public String getName(){return this.name;}
    public String getDescription(){return this.description;}
    public Integer getQuantity(){return this.quantity;}
}
