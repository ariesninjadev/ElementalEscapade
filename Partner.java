import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Partner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Partner extends Mover
{
    
    NData ndata;
    
    public String whoAmI = "partner";
    
    private String activeCostume = "partner-still.png";
    private GreenfootImage image;
    private boolean facing = true;
    
    Thread thread;
    
    public int health=100;

    public void act() {}
    
    public void init(String who) {
        setImage(who + "-still.png");
        orient(true);
        whoAmI = who;
    }
    
    public void switchCostume(String costume) {
        activeCostume = costume;
        orient(facing);
    }
    
    public void orient(boolean dir) {
        setImage(activeCostume);
        GreenfootImage img = getImage();
        int sizer = 2;
        img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
        if (!dir) {
            img.mirrorHorizontally();
        }
        setImage(img);
    }
    
    // DOCUMENTATION FOR NET NOT PROVIDED
    public void partnerGet()
    {
        thread = new Thread(new Runnable() 
        {
            public void run() 
            {
                while (true) {
                    try
                    {
                        ndata = Network.getPartnerLocation();
                    }
                    catch (java.io.IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    setLocation(ndata.x,ndata.y);
                    facing = ndata.facing;
                    if (ndata.idle) {
                        switchCostume(whoAmI + "-idle.png");
                    } else {
                        switchCostume(whoAmI + "-still.png");
                    }
                }
            }
        });
        thread.start();
    }
    
    public void stopGet()
    {
        try {
            thread.stop();
        } catch (java.lang.NullPointerException npe) {}
    }
    
    public void hit(int damage)
    {
        health-=damage;
        if (health<=0)
        {
            Game game=(Game) getWorld();
            game.restartLevel();
            health=100;
        }
    }
}
