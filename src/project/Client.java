
package project;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {    
    Socket socket;
    BufferedReader input;
    BufferedReader keyboard;
    PrintWriter output;

    public Client() throws IOException{
        this.socket = new Socket("127.0.0.1", 9082);                                      //Skapar socketen
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));     //Läser från servern
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));                //Behövs för att läsa från tangetbordet
        this.output = new PrintWriter(socket.getOutputStream(), true);               //Skriver till servern
        
        new Thread(new Listen()).start();
        new Thread(new Send()).start();
    }
    
    public class Listen implements Runnable {
        
        @Override
        public void run() {
            try {
                while(socket.isConnected()){
                    String serverResponse = input.readLine();                                          //Får responsen från servern och sparar den i serverResponse
                    if(serverResponse == null) {
                        System.out.println("The server is down!");
                        break;
                    }
                    System.out.println(serverResponse); 
                    
                }
            } catch (IOException e){
            }
        }
    }

    public class Send implements Runnable {
        @Override
        public void run() {
            try {
                while(socket.isConnected()) {
                    String command = keyboard.readLine();                                               //Läser från tangetbordet
                    if(command == null) return;
                    output.println(command);                                                            //Skickar till servern
                    output.flush(); 
                }
            }catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        
        //socket.close();        
        //System.exit(0);
    }
}


