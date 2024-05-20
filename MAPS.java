import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MAPS here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MAPS extends World
{

    /**
     * Constructor for objects of class MAPS.
     * 
     */
    public MAPS()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, false); 
    }
    
    public void loadWorld(int[][] num)
    {
        for (int i=0;i<num.length;i++)
        {
            for (int g=0;g<num[i].length;g++)
            {
                int x=g*20+10;
                int y=i*20+10;
                Tile tile = TileMap.getTile(num[i][g]);
                if (tile!=null)
                {
                    addObject(tile,x,y);
                }
            }
        }
    }
}
