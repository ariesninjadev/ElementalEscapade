import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Static here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Static extends Actor
{
    
    private boolean watchForHover = false;
    private boolean hover = false;
    private boolean mouseOver = false;
    private int origin = 0;
    private boolean direction = true;
    private GreenfootImage image;
    private Runnable onClick;
    private boolean clock = true;
    
    /**
     * Act - do whatever the Static wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        clock = !clock;
        if (hover && clock) {
            int scale = direction ? 1 : -1;
            setLocation(getX(),getY()+scale);
            if (getY()==origin+(4*scale)) {
                direction = !direction;
            }
        }
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
    
    public void display(String text, String action) {
        origin = getY();
        setImage(text);
        hover = true;
    }
}
