import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Minion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Minion extends Mover
{
    /**
     * Act - do whatever the Minion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int distance;
    private int distancecount=0;
    private int startx;
    private boolean direction;
    public static final int speed = 1;
    
    
    public Minion(String name, int length, int x)
    {
        setImage(new GreenfootImage(name));
        distance=length;
        startx=x;
        direction=true;
    }
    public void act()
    {
        move();
        checkFall();
    }
    public void move()
    {
        if(direction) {
            moveRight(speed, false);
        } else {
            moveLeft(speed, false);
        }
        
        if(getX() == startx+distance) {
            direction = !direction;
        }
    }
    private void checkFall()
    {
        if (onGround()) {
            setVSpeed(0);
            this.jumpCount = 0;
        }
        else {
            if (!fall()) {
                this.jumpCount = 0;
            }
        }
    }
    public void moveLeftOld() {
        int s = speed;
        for (int step = 0; step <= s; step += 1) {
            setLocation(getX() - 1, getY());
            List<Actor> actors = getIntersectingObjects(null);
            boolean colliding = false;
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
            if (colliding) {
                setLocation(getX() + 1, getY());
                if (jumpCount == 0) {
                    jump();
                    jumpCount++;
                }
                return;
            }
        }
    }
    public void moveRightOld() {
        int s = speed;
        for (int step = 0; step <= s; step += 1) {
            setLocation(getX() + 1, getY());
            List<Actor> actors = getIntersectingObjects(null);
            boolean colliding = false;
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
            if (colliding) {
                setLocation(getX() + 1, getY());
                if (jumpCount == 0) {
                    jump();
                    jumpCount++;
                }
                return;
            }
        }
    }
}
