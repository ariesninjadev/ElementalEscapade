import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Earth1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Earth1 extends MAPS
{
    /**
     * Constructor for objects of class Earth1.
     * 
     */
    
    Player me;
    Partner you;
    
    public Earth1()
    {    
        //super(600, 400, 1);
        me=new Player();
        addObject(me,50,328);
        
        loadWorld(Data.earth1);
        
    }
}
