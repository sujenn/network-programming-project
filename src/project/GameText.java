
package project;

public class GameText {
    
    public GameText() {
        
    }
    
    public String welcomeText() {
        return "\nWELCOME TO THE ROOM!\nTo reach into your pocket write: SEE POCKET\nTo exit the game write: EXIT GAME\nTo choose an alternative write the number of the alternative.\n";

    }
    
    public String[] start() {
        String[] textAndOptions = {"\nI am standing in the middle of the room.\nWhich wall should I walk towards?","1.NORTH 2.WEST 3.EAST 4.SOUTH"};
        return textAndOptions;
    }
    
    public String start2() {
        return "I am standing in the middle of the room.\nWhich wall should I walk towards?\n1.NORTH 2.WEST 3.EAST 4.SOUTH";
    }
    
    public String[] north() {
        String[] textAndOptions = {"\nThe NORTH wall, cool!\nShould I make myself a cup of fresh coffee or see what is on that painting?","1.GET IN LINE FOR COFFEE 2.LOOK AT THE PAINTING 3.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] west() {
        String[] textAndOptions = {"\nThe WEST wall, interesting!\nWOW a bookshelf to the left and a big window to the right.","1.LET'S READ SOME BOOKS 2.LET'S LOOK OUT THE WINDOW 3.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] east() {
        String[] textAndOptions = {"\nThe EAST wall, nice!\nA cozy couch. Should I talk to the people sitting in the couch?","1.BE SOCIAL 2.I SHOULD GO BACK"};
        return textAndOptions;
    }
            
    public String[] south() {
        String[] textAndOptions = {"\nThe SOUTH wall, great!\nWhat a large desk!","1.I SHOULD SIT ON THE DESK CHAIR AND LOOK CLOSER AT THE ITEMS ON THE DESK 2.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] painting() {
        String[] textAndOptions = {"\nWhat a beautiful painting of a green field with four grazing cows! Painter: A Human.","1.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] coffee() {
        String[] textAndOptions = {"","1.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] bookshelf() {
        String[] textAndOptions = {"\nThe books on the shelf look really interesting.","1.I SHOULD TAKE A CLOSER LOOK 2.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] book() {
        String[] textAndOptions = {"","1.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] window() {
        String[] textAndOptions = {"","1.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] couch() {
        String[] textAndOptions = {"","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }
    
    public String[] desk() {
        String[] textAndOptions = {"\nThe chair is very nice to my butt. And WOW so much stuff on the desk! \nShould I take anything and put it in my pocket? Or maybe write something in the note pad?","1.PICK UP PENCIL 2.PICK UP STAPLER 3.PICK UP PHONE 4.PICK UP DESK LAMP 5.WRITE IN NOTE PAD 6.I SHOULD GO BACK"};
        return textAndOptions;
    }
    
    public String[] pencil() {
        String[] textAndOptions = {"\nNice, the pencil is now in my pocket","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }
    
    public String[] stapler() {
        String[] textAndOptions = {"\nCool, I just placed the stapler in my pocket","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }
    
    public String[] phone() {
        String[] textAndOptions = {"\nI now have the phone in my pocket","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }
    
    public String[] desklamp() {
        String[] textAndOptions = {"\nNice, the desklamp is in my pocket","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }
    
    public String[] notepad() {
        String[] textAndOptions = {"","1.I SHOULD GO BACK"};
        return textAndOptions;    
    }

}
