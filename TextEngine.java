import greenfoot.*;

/**
 * Write a description of class TextEngine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TextEngine  
{

    World world;
    
    /**
     * Constructor for objects of class TextEngine
     */
    public TextEngine(World world)
    {
        this.world = world;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void show(String m_text, int x, int y, Color color, Color bgcolor, int size)
    {
        Text text = new Text();
        world.addObject(text, x, y);
        text.display(m_text, color, bgcolor, size);
    }
}
