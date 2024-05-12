import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Partner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Partner extends Mover
{
    
    int[] pdata;
    
    Thread thread;
    
    /**
     * Act - do whatever the Partner wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {}
    
    public void partnerGet()
    {
        thread = new Thread(new Runnable() 
        {
            public void run() 
            {
                while (true) {
                    try
                    {
                        pdata = Network.getPartnerLocation();
                    }
                    catch (java.io.IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    setLocation(pdata[0],pdata[1]);
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
}
