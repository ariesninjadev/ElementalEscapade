import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Menu extends World
{
    
    MenuBG menu1 = new MenuBG();
    MenuBG menu2 = new MenuBG();
    
    public void joinLobby() throws java.io.IOException {
        Greenfoot.setWorld(new Lobby());
    }
    
    /**
     * Constructor for objects of class Menu.
     * 
     */
    public Menu() throws java.io.IOException
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, false); 
        
        TextEngine text = new TextEngine(this);
        StaticEngine stat = new StaticEngine(this);
        
        addObject(menu1, 240, 200);
        addObject(menu2, -480, 200);
        
        stat.show("menu-title.png",280,180);
        stat.show("menu-play.png",120,180,true,(() -> 
            {
                try
                {
                    joinLobby();
                }
                catch (java.io.IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        ));
    }
}
