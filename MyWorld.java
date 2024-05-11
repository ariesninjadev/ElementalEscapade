import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{

    Player me;
    Partner you;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld() throws java.io.IOException
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, false); 
        
        int playerNum = 0;
        
        Alert alert = new Alert();
        me = new Player();
        you = new Partner();
        
        addObject(new Ground(), 300, 400);
        addObject(alert, 300, 20);
        addObject(me, 0, 800);
        addObject(you, 0, 800);
        
        String input = Greenfoot.ask("Game to join");
        
        String response = Network.joinGame(input);
        
        if (response.equals("0")) {
            alert.display("This game is full!", new Color(0,0,0,0), Color.ORANGE, 24);
        } else if (response.equals("1")) {
            alert.display(" Joined as player 1. ", new Color(0,0,0), Color.RED, 24);
            me.setImage("player.jpeg");
            you.setImage("partner.jpeg");
            me.setLocation(150, 200);
            playerNum = 1;
            me.startLocalPost();
            you.partnerGet();
        } else if (response.equals("2")) {
            alert.display(" Joined as player 2. ", new Color(0,0,0), Color.BLUE, 24);
            me.setImage("partner.jpeg");
            you.setImage("player.jpeg");
            me.setLocation(450, 200);
            playerNum = 2;
            me.startLocalPost();
            you.partnerGet();
        } else {
            alert.display("There was an error.", new Color(0,0,0,0), Color.ORANGE, 24);
        }
    }
    
    public void stopped()
    {
        me.stopLocalPost();
        you.stopGet();
    }
}
