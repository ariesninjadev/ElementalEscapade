import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Static here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Static extends Actor
{
    
    boolean watchForHover = false;
    boolean mouseOver = false;
    private GreenfootImage image;
    Runnable onClick;
    
    /**
     * Act - do whatever the Static wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (!watchForHover) {
            return;
        }
        if (!mouseOver && Greenfoot.mouseMoved(this)) {
            int sizer = 1;
            GreenfootImage img = new GreenfootImage(image);
            img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
            setImage(img);
            mouseOver = true;
        } else if (mouseOver && ! Greenfoot.mouseMoved(this) && Greenfoot.mouseMoved(null)) {
            int sizer = 0;
            GreenfootImage img = new GreenfootImage(image);
            img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
            setImage(img);
            mouseOver = false;
        }
        if (Greenfoot.mouseClicked(this)) {
            onClick.run();
        }
    }
    
    public void display(String text) {
        setImage(text);
    }
    
    public void display(String text, boolean hoverable, Runnable onClick) {
        setImage(text);
        image = getImage();
        watchForHover = hoverable;
        this.onClick = onClick;
    }
}
