import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lobby here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lobby extends MAPS
{

    Player me;
    Partner you;
    
    Static pressE;
        
    boolean inRange = false;
    
    TextEngine text = new TextEngine(this);
    StaticEngine stat = new StaticEngine(this);
    
    int playerNum = 0;
    boolean arrowShown = false;
    
    /**
     * Constructor for objects of class Lobby.
     * 
     */
    public Lobby(boolean mp) throws java.io.IOException
    {    
        //super(600, 400, 1, false); 
        
        GreenfootImage background = getBackground();
        background.setColor(new Color(212,245,255));
        background.fillRect(0,0,getWidth(),getHeight());
        
        loadWorld(Data.lobby);
        
        me = new Player();
        addObject(me, 0, 1000);
        
        setPaintOrder(Player.class);
        
        if (mp) {
            
            you = new Partner();
            addObject(you, 0, 1000);
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
                me.setImage("player.png");
                you.setImage("partner.png");
                me.setLocation(280, 200);
                playerNum = 1;
                me.startLocalPost();
                you.partnerGet();
            } else if (response.equals("2")) {
                text.show(" Joined as player 2. ", 300, 20, Color.BLUE, new Color(0,0,0), 24);
                me.setImage("partner.png");
                you.setImage("player.png");
                me.setLocation(320, 200);
                playerNum = 2;
                me.startLocalPost();
                you.partnerGet();
            } else {
                text.show("There was an error.", 300, 20, Color.ORANGE, new Color(0,0,0,0), 24);
            }
            
        } else {
            me.setLocation(300, 200);
            stat.show("darrow.png",550,310,"hover");
            arrowShown = true;
        }

    }
    
    public void stopped()
    {
        try {
        me.stopLocalPost();
        you.stopGet();
        } catch (Exception e) {}
    }
    
    public void act() {
        try {
            if (you.getY() != 1000 && playerNum == 1 && !arrowShown) {
                //System.out.println(you.getY());
                stat.show("darrow.png",550,310,"hover");
                arrowShown = true;
            }
        } catch (Exception e) {}
        if (me.getX() > 480 && me.getX() < 620 && me.getY() > 240 && arrowShown)
        {
            if (!inRange) {
                pressE = stat.show("press-e.png",534,10);
                inRange = true;
            }
        } else if (inRange) {
            removeObject(pressE);
            pressE = null;
            inRange = false;
        }
    }
}
