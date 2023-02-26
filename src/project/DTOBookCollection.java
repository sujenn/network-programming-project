package project;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents all of the books retrieved from database (table BOOK), enables a random book to be selected.
 * 
 */
public class DTOBookCollection {
    ArrayList<DTOBook> collection;
    
    
    public DTOBookCollection(){
        this.collection = new ArrayList<DTOBook>();
    }
    
    public void addBook(DTOBook book){
        collection.add(book);
    }
    
    /*
    Vilken returtyp behövs? Arraylist eller array? Låt det avgöras av behov, tillsvidare array
    */
    public DTOBook[] getBookCollection(){
        DTOBook[] bookarray = new DTOBook[this.collection.size()];  // en tom array måste allokeras innan ArrayList.toArray kan anropas
        return collection.toArray(bookarray);
    }
    
    /**
     * 
     * @return a randomly selected book
     */
    public DTOBook getRandomBook(){
        DTOBook[] bookarray = new DTOBook[this.collection.size()];  // en tom array måste allokeras innan ArrayList.toArray kan anropas
        bookarray = collection.toArray(bookarray);
        int randomIndex = new Random().nextInt(bookarray.length);   // slumpa fram en siffra mellan 0 och bookarray.length
        
        try{        // try-catch för att fånga bl.a. nullpointerexception
            return bookarray[randomIndex];
        }
        catch(Exception e){
            System.out.println("Bookcollection kunde inte returnera slumpad book");
            System.out.println(e);
        }
        return null;
    }
}
