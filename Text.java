import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Alert here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Text extends Actor
{
    int ttl = 250;
    
    public void act()
    {
        ttl--;
        if (ttl==0)
        {
            getWorld().removeObject(this);
        }
    }
    
    public void display(String text, Color color, Color bgcolor, int size) {
        setImage(new GreenfootImage(text, size, color, bgcolor));
    }
}
