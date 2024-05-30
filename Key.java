import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class Key extends Tile
{
    
    public static int v = -1;
    
    private boolean clock = false;
    private boolean direction = false;
    private int origin = 0;
    private boolean firstRun = true;
    
    private boolean pickedUpState = false;
    
    private int transp = 100;
    
    public Key() {
        v++;
        switch (v) {
            case 0:
                break;
            case 1:
                setImage("green-key.png");
                break;
            case 2:
                setImage("blue-key.png");
                break;
        }
    }
    
    public boolean isCollidable() {
        return false;
    }
    
    public void act()
    {
        if (firstRun) {
            origin = getY();
            firstRun = false;
        }
        if (((Game)getWorld()).noAnimate) {
            return;
        }
        clock = !clock;
        if (pickedUpState) {
            if (transp==0) {
                getWorld().removeObject(this);
                pickedUpState = false;
                return;
            }
            transp -= 5;
            getImage().setTransparency(transp);
            setLocation(getX(),getY()-1);
        } else if (clock) {
            int scale = direction ? 1 : -1;
            setLocation(getX(),getY()+scale);
            if (getY()==origin+(3*scale)) {
                direction = !direction;
            }
        }
        if (pickedUp() && !pickedUpState) {
            pickedUpState = true;
            ((Game)getWorld()).keyObtained();
            Greenfoot.playSound("key-pickup.wav");
        }
    }
    
    public boolean pickedUp() {
        List<Actor> actors = getIntersectingObjects(null);
        for (Actor a : actors) {
            if (a instanceof Player && !((Player)a).dead) {
                return true;
            }
        }
        return false;
    }
    
    public boolean pickedUpOld() {
        List<Actor> left1 = getObjectsAtOffset(-getImage().getWidth()/2-1,-getImage().getHeight()/2-1, null);
        for (Actor a : left1) {
            if (a instanceof Player || a instanceof Partner) {
                return true;
            }
        }
        List<Actor> right1 = getObjectsAtOffset(getImage().getWidth()/2+1,-getImage().getHeight()/2-1, null);
        for (Actor a : right1) {
            if (a instanceof Player || a instanceof Partner) {
                return true;
            }
        }
        List<Actor> left2 = getObjectsAtOffset(-getImage().getWidth()/2-1,getImage().getHeight()/2+1, null);
        for (Actor a : left2) {
            if (a instanceof Player || a instanceof Partner) {
                return true;
            }
        }
        List<Actor> right2 = getObjectsAtOffset(getImage().getWidth()/2+1,getImage().getHeight()/2+1, null);
        for (Actor a : right2) {
            if (a instanceof Player || a instanceof Partner) {
                return true;
            }
        }

        return false;
    }
}
