import greenfoot.*;

/**
 * Write a description of class TextEngine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StaticEngine  
{

    World world;
    
    
    
    /**
     * Constructor for objects of class TextEngine
     */
    public StaticEngine(World world)
    {
        this.world = world;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void show(String img, int x, int y)
    {
        Static staticX = new Static();
        world.addObject(staticX, x, y);
        staticX.display(img);
    }
    
    public void show(String img, int x, int y, boolean hoverable, Runnable onclick)
    {
        Static staticX = new Static();
        world.addObject(staticX, x, y);
        staticX.display(img,hoverable,onclick);
    }
}
