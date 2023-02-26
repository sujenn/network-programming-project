
package project;

import java.util.ArrayList;
import java.util.List;

/**
 * Server interagerar med en DAOInterface, implementationen är gömd i DAOImplementation.
 * Server måste skapa ett DAOInterface-objekt, men kan sedan anropa dessa metoder.
 * Metoderna utförs i DAOImplementationklassen.
 * Detta följer designmönstret DAO (data access object), som kopplar bort databasdesignen från resten av projektet
 */
public interface DAOInterface {
    /**
     * Create a new player in the database.
     * @param username  new player's username
     * @param password  new player's password
     */
    public void createNewPlayer(String username, String password);
    
    /**
     * Retrieves the saved position of a player in the database.
     * @param username
     * @return String   current position
     */
    public String getPlayerPosition(String username);
    
    /**
     * Update the player's position as stored in the database.
     * @param username  the player's username
     * @param position  the player's current position
     */
    public void setPlayerPosition(String username, Node position);
    
    /**
     * Used to verfy login attempts
     * @param username  a given username
     * @param password  a given password
     * @return          true if player exists amd password matches, false if not
     */
    public boolean verifyUsernamePassword(String username, String password);
    
    /**
     * Add a pocketable object to a player's pocket, or increment the quantoty of an existing object by 1.
     * @param username      the name of the object to be added into the pocket
     * @param objectname    the name of the player
     */
    public void updatePlayerPocket(String username, String objectname);
    
    /**
     * Update number of coffees a player has drunk.
     * @param username      the name of the player
     */
    public void updatePlayerCoffeesDrunk(String username);
    
    /**
     * Get contents of a player's pocket.
     * @param username  name of player
     * @return          List of contents of a player's pocket
     */
    public ArrayList<DTOPocketable> getPlayerPocket(String username);
    
    /**
     * Get a list of all Pocketable objects in the database.
     * @return          List of Pocketable objects in database
     */
    public List<DTOPocketable> getPocketablesFromDB();
    
    /**
     * Get all books in database, recieve a collection of books.
     * @return DTOBookCollection offers a method to retrieve one random book.
     */
    public DTOBookCollection getBooksFromDB();
    
    
    /**
     * Insert a new postitnote to the database.
     * @param user      String, the player's name
     * @param message   String, max 128 chars
     */
    public void insertNewPostIt(String user, String message);

    /**
     * Retrieve the 5 newest postitnotes.
     * @return a list of String, on the format "message    - user"
     */
    public ArrayList<String> getPostItNotes();
    
    /**
     * Get all views from a specified window.
     * @return DTOWindowView offers a methid to retrieve one random book
     */
    public DTOWindowView getWindowViewFromDB();
}
