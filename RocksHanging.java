import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class RocksHanging extends Tile
{
    
    int delay = Greenfoot.getRandomNumber(4);
    
    boolean executeEvent = false;
    
    int vSpeed = 0;
    
    int acceleration = 1;
    
    public boolean onGround = false;
    
    int aliveClock = 75;
    
    int transparency = 255;
    
    public void act()
    {
        if (aliveClock == 0 && !(transparency == 0)) {
            transparency-=3;
            getImage().setTransparency(transparency);
            return;
        }
        if (onGround) {
            aliveClock--;
            return;
        }
        if (!executeEvent) {
            return;
        }
        if (delay > 0) {
            delay--;
            return;
        }
        fall();
    }
    
    public String walkFile() {
        return "walk-grass.wav";
    }
    
    public void event() {
        executeEvent = true;
    }
    
    public boolean fall() {
        vSpeed += acceleration;
        int dir = (int) Math.signum(vSpeed);
        for (int step = 0; step != vSpeed; step += dir) {
            setLocation(getX(), getY() + dir);
            List<Actor> actors = getIntersectingObjects(null);
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    setLocation(getX(), getY() - dir + 6);
                    Greenfoot.playSound("land.wav");
                    setImage("fallen-rocks.png");
                    vSpeed = 0;
                    onGround = true;
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isCollidable() {
        return false;
    }
}
