package project;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents all of the books retrieved from database (table BOOK), enables a random book to be selected.
 * 
 */
public class DTOWindowView {
    ArrayList<String> windowView;
    
    
    public DTOWindowView(){
        this.windowView = new ArrayList<String>();
    }
    
    public void addView(String view){
        windowView.add(view);
    }
    
    /*
    Vilken returtyp behövs? Arraylist eller array? Låt det avgöras av behov, tillsvidare array
    */
    public String[] getAllViews(){
        String[] bookarray = new String[this.windowView.size()];  // en tom array måste allokeras innan ArrayList.toArray kan anropas
        return windowView.toArray(bookarray);
    }
    
    /**
     * 
     * @return a randomly selected book
     */
    public String getRandomView(){
        String[] viewarray = new String[this.windowView.size()];  // en tom array måste allokeras innan ArrayList.toArray kan anropas
        viewarray = windowView.toArray(viewarray);
        int randomIndex = new Random().nextInt(viewarray.length);   // slumpa fram en siffra mellan 0 och bookarray.length
        
        try{        // try-catch för att fånga bl.a. nullpointerexception
            return viewarray[randomIndex];
        }
        catch(Exception e){
            System.out.println("WindowView kunde inte returnera slumpad fönsterutsikt");
            System.out.println(e);
        }
        return null;
    }
}
