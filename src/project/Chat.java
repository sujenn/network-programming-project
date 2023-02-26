
package project;

import java.util.ArrayList;

public class Chat {
    ArrayList<Server.ClientHandler> chatMembers;
    
    public Chat() {
        chatMembers = new ArrayList<Server.ClientHandler>();
    }
    
    public ArrayList<Server.ClientHandler> getChatMembers() {
        if(chatMembers.isEmpty() == true) {
            return null;
        }
        return chatMembers;
    }
    
    public void addChatMember(Server.ClientHandler username) {
        chatMembers.add(username);
    }
    
    public void removeChatMember(Server.ClientHandler username) {
        chatMembers.remove(username);
    }
}
