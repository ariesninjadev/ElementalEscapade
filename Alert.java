import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Alert here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Alert extends Actor
{
    /**
     * Act - do whatever the Alert wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
    
    public void display(String text, Color color, Color bgcolor, int size) {
        setImage(new GreenfootImage(text, size, bgcolor, color));
    }
}
