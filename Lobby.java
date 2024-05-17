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
    public Lobby(boolean mp) throws java.io.IOException
    {    
        super(600, 400, 1, false); 
        
        int playerNum = 0;
        
        TextEngine text = new TextEngine(this);
        
        addObject(new Ground(), 300, 400);
        
        me = new Player();
        addObject(me, 0, 800);
        
        if (mp) {
            you = new Partner();
            addObject(you, 0, 800);
        }
        
        setPaintOrder(Player.class);
        
        if (mp) {
            
            you = new Partner();
            addObject(you, 0, 800);
            String input = Greenfoot.ask("Game to join");
            String response = Network.joinGame(input);
            
            if (response.equals("-1")) {
                text.show("Can't connect to the server!", 300, 20, Color.ORANGE, new Color(0,0,0,0), 24);
                Greenfoot.stop();
                return;
            } else if (response.equals("0")) {
                text.show("This game is full!", 300, 20, Color.ORANGE, new Color(0,0,0,0), 24);
                Greenfoot.stop();
                return;
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
            
        } else {
            me.setLocation(150, 200);
        }
        
        // Draw Lobby
        
        addObject(new Ground(), 300, 400);
        addObject(new CollidableTest(), 50, 310);
        addObject(new CollidableTest(), 150, 250);
        addObject(new CollidableTest(), 250, 175);
        addObject(new CollidableTest(), 350, 130);
    }
    
    public void stopped()
    {
        try {
        me.stopLocalPost();
        you.stopGet();
        } catch (Exception e) {}
    }
}
