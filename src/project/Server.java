
package project;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Server {
    private ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>(); 
    private ArrayList<String> queue = new ArrayList<String>();
    private int clientNbr;
    private DAOImplementation database;

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9082);
        clientNbr = 0;
        Chat chat = new Chat();
        database = new DAOImplementation();
        
        while(true){
            System.out.println("Server waiting for client to connect...");
            Socket client = serverSocket.accept();
            System.out.println("Client connected"); 
            clientNbr++;
            ClientHandler clients = new ClientHandler(client, this, chat);             
            clientList.add(clients);                                                         
            new Thread(clients).start();
        }
    }
    
    public class ClientHandler implements Runnable {
        private Socket client;
        public String clientName;  
        private PrintWriter output;
        private Server server;
        private Node position;
        private String[] textAndOptions;
        private boolean loggedIn;
        private Chat chat;
        
        public ClientHandler(Socket clientSocket, Server server, Chat chat) {
            this.client = clientSocket;
            this.server = server;
            this.position = null;
            this.textAndOptions = null;
            this.loggedIn = false;
            this.chat = chat;

            try {
                this.output = new PrintWriter(client.getOutputStream(), true);
            } 
            catch (IOException e){
            }
        }
    
        @Override
        public void run() {
            try{
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));     
                GameText gt = new GameText();
                RoomModel rm = new RoomModel(textAndOptions);
                
                login(input, rm, gt);
                
                while(client.isConnected()) {
                    String clientRequest = input.readLine();
                    
                    if(clientRequest.equals("EXIT GAME")) {
                        database.setPlayerPosition(clientName, rm.getPosition());
                        loggedIn = false;
                        clientName = null;
                        login(input, rm, gt);
                        
                    } else if(clientRequest.equals("SEE POCKET")) {
                        boolean inPocket = true;
                        output.println("Here is all the stuff in your pocket:");
                        //hämta alla objekt och dess antal från databas
                        ArrayList<DTOPocketable> objects = database.getPlayerPocket(clientName);
                        for(int i = 0; i < objects.size(); i++) {
                            output.println(String.format("%s of %s (%s)", objects.get(i).getQuantity(), objects.get(i).getName(), objects.get(i).getDescription()));
                            output.flush();
                        }
                        output.println("To exit pocket write: EXIT POCKET");
                        output.flush();
                        while(inPocket == true){
                            if(input.readLine().equals("EXIT POCKET")){
                                inPocket = false;
                            }
                        }
                        output.println(rm.getText());
                        output.flush();
                        output.println(rm.getOptions());
                        output.flush();
                        
                    } else {
                    
                        boolean verify = rm.verifyOption(clientRequest);
                        if(verify == false) {
                            output.println("Choose one of the existing alternatives by entering the number.");
                            output.flush();
                            output.println(rm.getOptions());
                            output.flush();

                        } else {
                            position = rm.getPosition();                       
                            position = position.getConnections()[Integer.valueOf(clientRequest) - 1];   //Change to new position
                            rm.setPosition(position);                                                   //Set new position
                            
                            if(rm.getPosition().getName().equals("couch")){
                                chat.addChatMember(this);   //Lägger till denna client i chatt-listan
                                boolean chatAlive = true;
                                ArrayList<ClientHandler> chatMembers = chat.getChatMembers();
                                output.println("Welcome to Couch-Chat!\nTo exit chat write: EXIT CHAT");
                                //SIGNALER ATT NÅGON HAR GÅTT MED  I CHATT
                                for(int i = 0; i < chatMembers.size(); i++) {
                                    if(chatMembers.get(i) != this) {
                                        chatMembers.get(i).output.println(clientName + " has entered the chat!");   //KAN BARA ANVÄNDA SEND-METODEN PÅ ETT CLIENTHANDLER-OBJEKT
                                        output.flush();
                                    }
                                }
                                while(chatAlive == true) {
                                    String message = input.readLine(); 
                                    if(message.equals("EXIT CHAT")) {
                                        chat.removeChatMember(this);
                                        chatAlive = false;
                                    } else {
                                        //printa till alla som är med i chatten
                                        chatMembers = chat.getChatMembers();

                                        if(message != null) {       
                                            for(int i = 0; i < chatMembers.size(); i++) {
                                                if(chatMembers.get(i) != this) {
                                                    chatMembers.get(i).send(message, clientName);   
                                                }
                                            }
                                        }
                                    }
                                }
                                setNewText(rm);

                            } else if(rm.getPosition().getName().equals("coffee")) {
                                output.println("You are waiting in line for COFFEE! To exit queue write: EXIT QUEUE");
                                
                                Boolean threadAlive = true;
                                QueueHandler qh = new QueueHandler(clientName, output);
                                queue.add(clientName);
                                new Thread(qh).start();
                                while(threadAlive == true) {
                                    String answer = "";
                                    boolean validAnswer = false;
                                    while(validAnswer == false) {
                                        answer = input.readLine();
                                        if(((answer.equals("1") || answer.equals("2")) && qh.coffeeDone == true) || answer.equals("EXIT QUEUE")) {
                                            validAnswer = true;
                                        }
                                    }
                                     if(answer.equals("EXIT QUEUE")){
                                        qh.closeThread();
                                        queue.remove(clientName);
                                        threadAlive = false;
                                    } else if(Integer.valueOf(answer) == 1) {
                                        //Drink coffee
                                        output.println("Yum! That was a nice cup of coffee. Let's put the empty cup in the pocket.");
                                        //Lägg till tom kaffekopp i databas
                                        database.updatePlayerCoffeesDrunk(clientName);
                                        //database.updatePlayerPocket(clientName, "empty coffee mug");
                                        threadAlive = false;
                                    } else {
                                        //Lägg kaffekoppen i pocket 
                                        database.updatePlayerPocket(clientName, "filled coffee mug");
                                        threadAlive = false;
                                    }
                                }
                                setNewText(rm);
                                
                            } else if(rm.getPosition().getName().equals("pencil") || rm.getPosition().getName().equals("phone") || rm.getPosition().getName().equals("stapler") || rm.getPosition().getName().equals("desklamp")) {
                                //öka objekt-counter i databas för spelaren
                                addToDb(rm.getPosition().getName(), rm);

                            } else if(rm.getPosition().getName().equals("notepad")) {
                                // Hämta de fem senaste meddelanden sparade i anteckningsboken
                                output.println("Welcome to Note Pad!\nTo exit Note Pad write: EXIT NOTE PAD");
                                ArrayList<String> messages = (ArrayList) database.getPostItNotes();
                                for(int i = messages.size() -1; i >= 0; i--) {
                                    output.println(messages.get(i));
                                }
                                
                                //Listan med de fem meddelanden i terminalen uppdateras bara när man går in i note pad
                                boolean notepadAlive = true;
                                while(notepadAlive == true) {
                                    String noteMessage = input.readLine(); 
                                    if(noteMessage.equals("EXIT NOTE PAD")) {
                                        notepadAlive = false;
                                    } else {
                                        //Spara meddelandet, noteMessage, till databasen
                                        database.insertNewPostIt(clientName, noteMessage);
                                    }
                                }
                                setNewText(rm);
                               
                            } else if(rm.getPosition().getName().equals("book")) {
                                DTOBookCollection books = database.getBooksFromDB();
                                DTOBook book = books.getRandomBook();
                                output.println("\nBook title: " + book.getTitle() + "\nDescription: " + book.getDescription());
                                output.println("To put back the book write: PUT BACK");
                                boolean reading = true;
                                while(reading == true) {
                                    if(input.readLine().equals("PUT BACK")) {
                                        reading = false;
                                    }
                                }
                                setNewText(rm);
                                
                            } else if(rm.getPosition().getName().equals("window")) {
                                DTOWindowView view = database.getWindowViewFromDB();
                                output.println(view.getRandomView());
                                output.println("Don't won't to look anymore? write: THANKS FOR THE VIEW");
                                boolean lookingOutWindow = true;
                                while(lookingOutWindow == true) {
                                    if(input.readLine().equals("THANKS FOR THE VIEW")) {
                                        lookingOutWindow = false;
                                    }
                                }
                                setNewText(rm);
                                
                            } else {
                                setNewText(rm);
                            }

                        }
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }   
        }

        public void send(String msg, String name) throws IOException {
            String message = name + ": "+ msg;
            output.println(message);
            output.flush();
        }
        
        public void addToDb(String object, RoomModel rm) {
            //Lägg till objekteti databasen (öka counter för objektet)
            database.updatePlayerPocket(clientName, object);
            
            setNewText(rm);
        }
        
        public void login(BufferedReader input, RoomModel rm, GameText gt) throws IOException{
            while(loggedIn == false) {
                output.println("Username: ");
                output.flush();
                String username = input.readLine();
                output.println("Password: ");
                String password = input.readLine();
                output.flush();
                boolean valid = database.verifyUsernamePassword(username, password);
                if(valid){
                    clientName = username;
                    loggedIn = true;
                }
            }
            
            String nodeName = database.getPlayerPosition(clientName);
            Node[] allNodes = rm.getAllNodes();
                for(int i = 0; i < allNodes.length; i++) {
                    if(allNodes[i].getName().equals(nodeName)) {
                        textAndOptions = allNodes[i].getGameText();
                        position = allNodes[i];
                        rm.setPosition(allNodes[i]);
                        break;
                    }
                }
                rm.setTextAndOptions(textAndOptions);
                output.println(gt.welcomeText());
                output.flush();
                output.println(rm.getText());
                output.flush();
                output.println(rm.getOptions());
                output.flush();
        }
        
        public void setNewText(RoomModel rm) {
            textAndOptions = position.getGameText();       
            rm.setTextAndOptions(textAndOptions);        
            output.println(rm.getText());
            output.flush();
            output.println(rm.getOptions());
            output.flush();
        }
    }
    
    public class QueueHandler implements Runnable {
        private String clientName;
        private PrintWriter output;
        private boolean running;
        public boolean coffeeDone;
        
        public QueueHandler(String clientName, PrintWriter output) {
            this.clientName = clientName;
            this.output = output;
            this.running = true;
            this.coffeeDone = false;
        }
        
        @Override
        public void run() {
            try {
                boolean inQueue = true;
                while(inQueue == true) {
                    if(queue.indexOf(clientName) == 0) {
                        output.println("You are first in line! The COFFEE is brewing...");
                        TimeUnit.SECONDS.sleep(15);
                        queue.remove(clientName);
                        if(running == true) {
                            coffeeDone = true;
                            output.println("Your coffee is ready! Would you like to\n1.Drink the coffee or 2.Put it in your pocket");
                            queue.remove(clientName);
                        }
                        inQueue = false;
                    } else {
                        output.println("Your place in the queue: " + (queue.indexOf(clientName)+1));
                        //Skriver ut tiden man har väntat (vart femte sekund)
                        int timer = 0;
                        while(queue.indexOf(clientName) != 0) {
                            TimeUnit.SECONDS.sleep(1);
                            timer += 1;
                            if(running == true) {
                                output.println("You have been waiting in " + timer + "seconds\nYour place in the queue is: " + (queue.indexOf(clientName)+1));
                            } else {
                                inQueue = false;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                
            }
        }
        
        public void closeThread() {
            this.running = false;
        }
    }
 
    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}


