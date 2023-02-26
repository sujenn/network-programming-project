
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
* Implementationen av de databasanrop som tillgängliggörs via DAOInterface.
* 
* Syfte: genom att göra den tekniska implementationen i en klass och endast låta 
* resten av programmet interagera med DAOInterface kapslas de databasspecifika 
* bitarna in. Detta följer designmönstret DAO (data access object).
*/
public class DAOImplementation implements DAOInterface {
    //private PreparedStatement prep = null;
    //private ResultSet rs = null;
    
    // private static String urlstring = "jdbc:derby://localhost:1527/project/create=true"; 
    private static final String urlstring = "jdbc:derby://localhost:1527/project;create=true";
    private static final String driverName = "org.apache.derby.jdbc.ClientDriver";
    private static final String username = "admin";   
    private static final String password = "admin";
    
    private Connection connection;
    
    
    public DAOImplementation(){
        setUpConnection();
    }

    // anropas av konstruktor
    private void setUpConnection() {
        try {
            Class.forName(driverName);      // sätter drivermanager
            try {
                this.connection = DriverManager.getConnection(urlstring, username, password);
            } catch (SQLException ex) {
                System.out.println("Failed to create the database connection."); 
                System.out.println(ex);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("Driver not found."); 
            System.out.println(ex);
        }
    }
   
    // anropas när servern stängs ned
    private void closeConnection(){
        try { connection.close(); }
        catch (Exception e) {
            System.out.println("DAO Impl kan inte stänga connection");
        }
    }
    
    public boolean verifyUsernamePassword(String username, String password){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT password FROM player WHERE username=?";
        try{
            prep = connection.prepareStatement(querystring);
            prep.setString(1, username);
            rs = prep.executeQuery();
            
            String foundpass = null;
            boolean login = false;
            if(rs.next()){
                foundpass = rs.getString("password");
                System.out.println("DAO: password found in DB");
                login = true;
            }
            else { System.out.println(String.format("DAO: username %s not in table PLAYER", username));}
            
            if(login) return true;
            else return false;
            
        }
        catch(Exception e){}
        finally{
            
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return false;
    }
    
    // TODO not implemented
    public void createNewPlayer(String username, String password){
        // skapa och lägg in ny player i tabell player
        // skapa och lägg in ny playerstate i tabell playerstate, på position "start"
    }

    // returnera namnet på node genom att joina med tabell node
    @Override
    public String getPlayerPosition(String username){
        PreparedStatement prep = null;
        ResultSet rs = null;
        int player_id = getPlayerID(username);
        String querystring = "SELECT node.NODE_NAME, playerstate.PLAYER_ID FROM node JOIN playerstate ON node.NODE_ID=playerstate.NODE_ID WHERE playerstate.PLAYER_ID=?";
        
        String currentPlayerPosition = "";
        try{
            prep = connection.prepareStatement(querystring);
            prep.setInt(1, player_id);
            rs = prep.executeQuery();
            
            while(rs.next()){
                currentPlayerPosition = rs.getString("NODE_NAME");
            }
            System.out.println(String.format("getPlayerPosition hittade en position, returnerar %s", currentPlayerPosition));
            return currentPlayerPosition;
            // returnera resultat
        }
        catch(Exception e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    /*
    * För en given användare, updatera var användaren befinner sig (userstate).
    * @param    String username, String position
    */
    @Override
    public void setPlayerPosition(String username, Node position){
        // ur node, hämta positionsid
        String nodename = position.getName();
        int node_nbr = getNodeID(nodename);         // hämta id-nummer på node
        int player_id = getPlayerID(username);
        
        PreparedStatement prep = null;
        String querystring = "UPDATE playerstate SET playerstate.NODE_ID=? WHERE playerstate.PLAYER_ID=?";
        
        try{
            prep = connection.prepareStatement(querystring);
            prep.setInt(1, node_nbr);
            prep.setInt(2, player_id);
            int response = prep.executeUpdate();    // returnerar 1 om 1 rad ändrats, annars 0
            
            if(response > 0) {System.out.println(String.format("SetPlayerPosition lyckades ändra position till %s, för spelare %s",nodename, username));} // spårutskrift om vi lyckas ändra position
        }
        catch(Exception e){}
        finally{
            try { prep.close(); } catch (SQLException ignore) {}
        }
    }
   
    
    /*
    lägg till ett (1) objekt i fickan (ny rad, eller uppdatera kvantitet med +1)
    */
    @Override
    public void updatePlayerPocket(String username, String objectname){
        Integer player_nr = getPlayerID(username);
        Integer pocketRow = getPocketRow(player_nr, objectname);
        
        // pocketable av denna sort finns redan i fickan, öka antal med 1
        if(pocketRow!=null){
            // annars, öka kvantitet med 1
            incrementPocketContentQuantity(pocketRow);
        }
        // inga pocketables av denna sort finns, skapa ny rad i pocketcontent
        else{
            PreparedStatement prep = null;
            String querystring = "INSERT INTO pocketcontent (player_no, object_name, quantity) VALUES (?, ?, ?)";
        
            try{
                prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
                prep.setInt(1, player_nr);
                prep.setString(2, objectname);
                prep.setInt(3, 1);                                  // default, lägg in 1 objekt av denna typ i fickan
                int response = prep.executeUpdate();                // returnerar 1 om en rad läggs in i tabell, annats 0.
            
                if(response > 0){ System.out.println("updatePlayerPocket lyckades lägga till objekt");}
                else{ System.out.println("updatePlayerPocket misslyckades");  } 
            }
            catch(Exception e){}
            finally{
                try { prep.close(); } catch (SQLException ignore) {}
            }
        }
    }
    
    /*
    Hämta en samling av objekt som representerar det som ligger i spelarens ficka
    */
    @Override
    public ArrayList<DTOPocketable> getPlayerPocket(String username){
        // ur player, hämta player_id
        // ur pocketcontent, hämta alla rader som matchar player_id
        // ur pocketable, hämta namn+desc på alla objekt som hämtats
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring  = "SELECT pocketable.OBJECT_NAME, pocketable.OBJECT_DESCRIPTION,"
                + " pocketcontent.QUANTITY  FROM pocketcontent LEFT JOIN pocketable "
                + " ON pocketcontent.object_name=pocketable.object_name "
                + " WHERE player_no IN "
                + " (SELECT player.player_id FROM player WHERE username=?)";
        
        ArrayList<DTOPocketable> list = new ArrayList<DTOPocketable>();         // tom lista av Pocketables
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setString(1, username);
            rs = prep.executeQuery();
            
            // för varje resultatrad, skapa ett pocketable-objekt och lägg till i lista
            while(rs.next()){
                DTOPocketable pocketable = new DTOPocketable(rs.getString("object_name"), rs.getString("object_description"), rs.getInt("quantity"));
                list.add(pocketable);
            }
            return list;
        }
        catch(Exception e){
            System.out.println(e);
        }
        /*finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }*/
        return null;
    }
    
    @Override
    public void updatePlayerCoffeesDrunk(String playername){
        String filledMug = "filled coffee mug";
        String emptyMug = "empty coffee mug";
        
        int player_id = getPlayerID(playername);
        
        Integer emptyMugsPocketRow = getPocketRow(player_id, emptyMug);     //returnerar pocketRow om en spelaren redan har tomma muggar i fickan, annars NULL
        if(emptyMugsPocketRow == null){
            updatePlayerPocket(playername, emptyMug);           // spelare har inga tomma kaffekoppar i fickan, skapa ny rad
        }
        else {
            incrementPocketContentQuantity(emptyMugsPocketRow); // spelare har redan tomma kaffekoppar i fickan, inkrementera antalet
        }
        // inkrementera antalet kaffekoppar som användaren har druckit
        playerDrinksCoffee(player_id);
    }
    
    @Override
    public void insertNewPostIt(String user, String message){
        PreparedStatement prep = null;
        String querystring = "INSERT INTO postitnote (message, player_no) VALUES (?, ?)";
        
        Integer playerNbr = getPlayerID(user);               // kringgå getPlayerID, den funkar inte?
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setString(1, message);                         // sätt första variabeln
            prep.setInt(2, playerNbr);                          // sätt andra variabeln
            // prep.setInt(2, 3);                                  // hårdkodat till admin istället för rätt användare
            int response = prep.executeUpdate();                // executeUpdate för inserts, updates, deletes. Returnerar 0 om ingen rad påverkas, 1 om en rad lagts till
            if(response > 0){ System.out.println("insertNewPostIt lyckades!");}
        }
        catch(Exception e){}
        finally{
            try { prep.close(); } catch (SQLException ignore) {}
        }
    }

    /**
     * 
     * @return a list of the 5 newest postits on format "message.    - username"
     */
    @Override
    public ArrayList<String> getPostItNotes(){
        PreparedStatement prep = null;
        ResultSet rs = null;
        // hämta kolumner postit_id, message, username från tabellerna postitnote+player, ordna i fallande ordning med nyast högst upp, hämta ut fem översta
        String querystring = "SELECT postitnote.POSTIT_ID, postitnote.MESSAGE, "
                + " player.USERNAME FROM postitnote JOIN player ON "
                + " postitnote.PLAYER_NO=player.PLAYER_ID ORDER BY "
                + " postitnote.POSTIT_ID DESC FETCH FIRST 5 ROWS ONLY";
        
        ArrayList<String> list = new ArrayList<String>();                               // tom lista med postitmeddelanden
        String postit = "";                                     // variabel för att lägga konstruerade svar till lista
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            rs = prep.executeQuery();
            System.out.println("getPostItNotes lyckades köra query");
            while(rs.next()){
                //System.out.println("inne i rs.next");
                postit = String.format("%s", rs.getString("message"));
                //System.out.println("hämtade ut sträng");
                list.add(postit);
                //System.out.println("lade till i lista");
                
                //System.out.println(postit);
            }
            
            return list;
        }
        catch(Exception e){}
        finally{
            try { rs.close(); } catch (Exception ignore) { }
            try { prep.close(); } catch (Exception ignore) {}
        }
        return null;
    }
    
    /*
    returnera en lista på ALLA pocketables i tabellen pocketable
    */
    @Override
    public List<DTOPocketable> getPocketablesFromDB(){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM pocketable";
        
        List<DTOPocketable> list = null;         // tom BookCollection
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            rs = prep.executeQuery();
            
            while(rs.next()){
                DTOPocketable pocketable = new DTOPocketable(rs.getString("object_name"), rs.getString("object_description"), 0);
                list.add(pocketable);
            }
            return list;
        }
        catch(Exception e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    /*
    Hämtar alla böcker i databas, lägger i en lista av Book-objekt
    */
    @Override
    public DTOBookCollection getBooksFromDB(){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM book";
        
        DTOBookCollection bookCollection = new DTOBookCollection();         // tom BookCollection
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            rs = prep.executeQuery();
            
            while(rs.next()){
                DTOBook book = new DTOBook(rs.getString("book_name"), rs.getString("book_desc"));
                bookCollection.addBook(book);
            }
            return bookCollection;
        }
        catch(SQLException e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    @Override
    public DTOWindowView getWindowViewFromDB(){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM windowview";
        
        DTOWindowView views = new DTOWindowView();
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            rs = prep.executeQuery();
            
            while(rs.next()){
                views.addView(rs.getString("view_desc"));
            }
            return views;
        }
        catch(SQLException e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return null;
    }
    
    
    ////***** HELPERS
    
    private int getPlayerID(String playername){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM player WHERE username=?";
        Integer player_id = 0;
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setString(1, playername);
            rs = prep.executeQuery();

            while(rs.next()){
                player_id = rs.getInt("player_id");     // hämta info från kolumn 'player_id'
            }
            return player_id;
        }
        catch(SQLException e){}
        finally{
            try { rs.close(); } catch (Exception ignore) { }
            try { prep.close(); } catch (Exception ignore) {}
        }
        return player_id;       // är null om inget svar hämtades ur databas.
    }  
    
    /**
     * Returnera pocket_id om det finns, annars null.
     * Används huvudsakligen för att ändra kvantitet i existerande rad
     * @param player_id
     * @param objectname
     * @return int identifier pocket_id in table pocketcontent or NULL;
     */
    private Integer getPocketRow(int player_id, String objectname){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM pocketcontent WHERE player_no=? AND object_name=?";
        Integer pocketRow = null;
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setInt(1, player_id);        // sätt första variabeln
            prep.setString(2, objectname);      // sätt andra variabeln
            rs = prep.executeQuery();
            
            while(rs.next()){
                pocketRow = rs.getInt("pocket_id");     // hämta info från kolumn 'player_id'
            }
            return pocketRow;
        }
        catch(SQLException e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return pocketRow;
    }
    
    private boolean isThisInPlayerPocket(String username, String objectname){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM pocketcontent WHERE object_name=? AND player_no IN (SELECT player.player_id FROM player WHERE username=?)";
        boolean exists = false;
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setString(1, objectname);        // sätt första variabeln
            prep.setString(2, username);      // sätt andra variabeln
            rs = prep.executeQuery();
            
            if(rs.next()){
                exists = true;     // hämta info från kolumn 'player_id'
            }
            return exists;
        }
        catch(Exception e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return exists;
    }
    
    /**
     * Increase quantity of a POCKETCONTENT row by +1.
     * @param pocketRow Integer. Row identifier.
     */
    private void incrementPocketContentQuantity(Integer pocketRow){
        PreparedStatement prep = null;
        String querystring = "update pocketcontent set quantity=quantity+1 where pocket_id=?";
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setInt(1, pocketRow);        // sätt första variabeln
            prep.executeUpdate();
        }
        catch(Exception e){}
        finally{
            try { prep.close(); } catch (SQLException ignore) {}
        }
    }
    
    /**
     * Decrease quantity of a POCKETCONTENT row by -1.
     * @param pocketRow Integer. Row identifier.
     */
    private void decrementPocketContentQuantity(Integer pocketRow){
        PreparedStatement prep = null;
        String querystring = "update pocketcontent set quantity=quantity-1 where pocket_id=?";
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setInt(1, pocketRow);        // sätt första variabeln
            prep.executeUpdate();
        }
        catch(Exception e){}
        finally{
            try { prep.close(); } catch (SQLException ignore) {}
        }
    }
    
    private Integer getNodeID(String nodename){
        PreparedStatement prep = null;
        ResultSet rs = null;
        String querystring = "SELECT * FROM node WHERE node_name=?";
        Integer nodeNbr = null;
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setString(1, nodename);
            rs = prep.executeQuery();
            
            while(rs.next()){
                nodeNbr = rs.getInt("NODE_ID");
            }
            return nodeNbr;
        }
        catch(Exception e){}
        finally{
            try { rs.close(); } catch (SQLException ignore) { }
            try { prep.close(); } catch (SQLException ignore) {}
        }
        return nodeNbr;
    }
    
    //
    private void playerDrinksCoffee(int player_id){
        PreparedStatement prep = null;
        String querystring = "UPDATE playerstate SET "
                + " playerstate.cups_consumed=cups_consumed+1 WHERE player_id=?";
        
        try{
            prep = connection.prepareStatement(querystring);    // lägg in sträng i preparedstatement
            prep.setInt(1, player_id);                      // sätt första variabeln
            prep.executeUpdate();
        }
        catch(Exception e){}
        finally{
            try { prep.close(); } catch (SQLException ignore) {}
        }
    }
}

