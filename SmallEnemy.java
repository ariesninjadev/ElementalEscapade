import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Minion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SmallEnemy extends Mover
{
    /**
     * Act - do whatever the Minion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private boolean direction = true;
    private boolean spriteFacing = false;
    public static final int speed = 1;
    
    private int damageDealt=15;
    public SmallEnemy(String name)
    {
        setImage(new GreenfootImage(name));
    }
    public void act()
    {
        move();
        checkFall();
        checkHit();
    }
    public void move()
    {
        if (((Game)getWorld()).noAnimate) {
            return;
        }
        if(direction) {
            boolean atWall = moveRight(speed, false);
            if (atWall) {
                direction = !direction;
            }
        } else {
            boolean atWall = moveLeft(speed, false);
            if (atWall) {
                direction = !direction;
            }
        }
        if (spriteFacing != direction) {
            spriteFacing = direction;
            GreenfootImage img = getImage();
            img.mirrorHorizontally();
            setImage(img);
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
    
    public void checkHit()
    {
        List<Partner> partners = getIntersectingObjects(Partner.class);
        List<Player> players = getIntersectingObjects(Player.class);
        for (Player player:players)
        {
            player.hit(damageDealt);
        }
        for (Partner partner:partners)
        {
            partner.hit(damageDealt);
        }
    }
    
}
