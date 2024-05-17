import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Earth1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Earth1 extends World
{
    /**
     * Constructor for objects of class Earth1.
     * 
     */
    public Earth1(int FrameNum)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1);
        
    }
    
    public void setFrame(int[][] num)
    {
        for (int i=0;i<num.length;i++)
        {
            for (int g=0;g<num[i].length;i++)
            {
                int x=i*20;
                int y=i*30;
                if (g==0)
                {
                    g++;
                }
                else if(g==1)
                {
                    addObject(new GroundTile(),x,y);
                }
                else if(g==2)
                {
                    addObject(new SandTile(),x,y);
                }
                else if(g==3)
                {
                    addObject(new GrassyGround(),x,y);
                }
                else if(g==4)
                {
                    addObject(new GroundTile(),x,y);
                }
                else if(g==5)
                {
                    
                }
            }
        }
    }
}
