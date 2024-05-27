import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Ocean extends Tile
{
    int clock = 0;
    boolean costume0 = true;
    
    public void act()
    {
        clock++;
        if (clock==25) {
            clock = 0;
            toggleCostume();
        }
    }
    
    private void toggleCostume() {
        if (costume0) {
            setImage("ocean-1.png");
        } else {
            setImage("ocean-0.png");
        }
        costume0 = !costume0;
    }
    
    public boolean isCollidable() {
        return false;
    }
}
