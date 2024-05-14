import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lobby here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lobby extends World
{

    Player me;
    Partner you;
    
    /**
     * Constructor for objects of class Lobby.
     * 
     */
    public Lobby() throws java.io.IOException
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, false); 
        
        int playerNum = 0;
        
        TextEngine text = new TextEngine(this);
        
        me = new Player();
        you = new Partner();
        
        addObject(new Ground(), 300, 400);
        addObject(me, 0, 800);
        addObject(you, 0, 800);
        
        addObject(new CollidableTest(), 150, 250);
        
        setPaintOrder(Player.class);
        
        String input = Greenfoot.ask("Game to join");
        
        String response = Network.joinGame(input);
        
        if (response.equals("0")) {
            text.show("This game is full!", 300, 20, Color.ORANGE, new Color(0,0,0,0), 24);
        } else if (response.equals("1")) {
            text.show(" Joined as player 1. ", 300, 20, Color.RED, new Color(0,0,0), 24);
            me.setImage("player.jpeg");
            you.setImage("partner.jpeg");
            me.setLocation(150, 200);
            playerNum = 1;
            me.startLocalPost();
            you.partnerGet();
        } else if (response.equals("2")) {
            text.show(" Joined as player 2. ", 300, 20, Color.BLUE, new Color(0,0,0), 24);
            me.setImage("partner.jpeg");
            you.setImage("player.jpeg");
            me.setLocation(450, 200);
            playerNum = 2;
            me.startLocalPost();
            you.partnerGet();
        } else {
            text.show("There was an error.", 300, 20, Color.ORANGE, new Color(0,0,0,0), 24);
        }
    }
    
    public void stopped()
    {
        me.stopLocalPost();
        you.stopGet();
    }
}
