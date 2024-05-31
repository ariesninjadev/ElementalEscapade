import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Mover
{
    /**
     * Act - do whatever the Boss wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage base;
    private String monster;
    public Boss(String name)
    {
        base= new GreenfootImage(name);
        monster=name;
        setImage(base);
    }
    public void act()
    {
        // Add your action code here.
    }
    public void idleAnimation()
    {
        for(int i=0;i%5==0;i++)
        {
            if (getImage()!=base)
            {
                setImage(base);
            }
            else
            {
                switch (monster) 
                {
                    case "Wicked-worm-idle-1":
                        break;
                    case "Super-squid-idle-1":
                        setImage(new GreenfootImage("Super-squid-idle-2"));
                        break;
                    case "Oscillating-fan-idle-1":
                        setImage(new GreenfootImage("Oscillating-"));
                        break;
                }
            }
            
            
        }
    }
}
