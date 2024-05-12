import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MenuBG here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MenuBG extends Actor
{
    
    boolean fSkip = true;
    
    /**
     * Act - do whatever the MenuBG wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (fSkip) {
            setLocation(getX()+1,getY());
            if (getX() >= 960) {
                setLocation(-480,getY());
            }
        }
        fSkip = !fSkip;
    }
}
