import java.util.List;

import greenfoot.*;

/**
 * The class Mover provides some basic movement methods. Use this as a
 * superclass
 * for other actors that should be able to move left and right, jump up and fall
 * down.
 */
public class Mover extends Actor {
    
    private static final int acceleration = 1;
    private static final int jumpStrength = 8;
    protected int jumpCount = 0;
    protected int headBoltTimer = 0;
    
    public boolean facing = true;

    public int vSpeed = 0;
    
    public boolean hasDrowned = false;
    
    public void orient(boolean bool) {}

    protected void jump() {
        setVSpeed(-jumpStrength);
        fall();
    }

    private boolean isColliding(int xOffset, int yOffset, Class<Actor> cls) {
        List<Actor> actors = getObjectsAtOffset(xOffset, yOffset, cls);
        for (Actor a : actors) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                return true;
            }
        }
        return false;
    }

    public boolean moveRight(int speed, boolean sprinting) {
        int s = sprinting ? speed * 2 : speed;
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
                setLocation(getX() - 1, getY());
                if (! (this instanceof Player)) {
                    return true;
                }
                if (jumpCount == 0 && spaceAdjacent()) {
                    Audio.playSound("jump.mp3");
                    jump();
                    jumpCount++;
                }
                return false;
            }
        }
        return false;
    }

    public boolean moveLeft(int speed, boolean sprinting) {
        int s = sprinting ? speed * 2 : speed;
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
                if (! (this instanceof Player)) {
                    return true;
                }
                if (jumpCount == 0 && spaceAdjacent()) {
                    Audio.playSound("jump.mp3");
                    jump();
                    jumpCount++;
                }
                return false;
            }
        }
        return false;
    }

    public boolean onGround() {
        return isColliding(0, getImage().getHeight() / 2, null);
    }
    
    public boolean submerged() {
        List<Actor> under = getObjectsAtOffset(0, getImage().getHeight() / 2 + 2, null);
        List<Actor> over = getObjectsAtOffset(0, -getImage().getHeight() / 2, null);
        for (Actor a : under) {
            if (a instanceof Wave || a instanceof Ocean) {
                return true;
            }
        }
        for (Actor a : over) {
            if (a instanceof Wave || a instanceof Ocean) {
                return true;
            }
        }

        return false;
    }

    public boolean onGroundExclusive() {
        List<Actor> under1 = getObjectsAtOffset(-getImage().getWidth() / 2, getImage().getHeight() / 2 + 1, null);
        //List<Actor> under2 = getObjectsAtOffset(0, getImage().getHeight() / 2 + 1, null);
        List<Actor> under3 = getObjectsAtOffset(getImage().getWidth() / 2, getImage().getHeight() / 2 + 1, null);
        boolean colliding = false;
        for (Actor a : under1) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                colliding = true;
                break;
            }
        }
        if (!colliding) {
            for (Actor a : under3) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
        }
        return colliding;
    }
    
    public Tile standingOn() {
        List<Actor> under = getObjectsAtOffset(0, getImage().getHeight() / 2 + 1, null);
        for (Actor a : under) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                return (Tile)a;
            }
        }
        return null;
    }

    public boolean atSide() {
        List<Actor> left1 = getObjectsAtOffset(-getImage().getWidth() / 2, getImage().getHeight() / 2 - 1, null);
        //List<Actor> left2 = getObjectsAtOffset(-getImage().getWidth() / 2, 0, null);
        List<Actor> left3 = getObjectsAtOffset(-getImage().getWidth() / 2, -getImage().getHeight() / 2 + 1, null);
        List<Actor> right1 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, getImage().getHeight() / 2 - 1, null);
        //List<Actor> right2 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, 0, null);
        List<Actor> right3 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, -getImage().getHeight() / 2 + 1, null);
    
        boolean colliding = false;
        for (Actor a : left1) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                colliding = true;
                break;
            }
        }
        if (!colliding) {
            for (Actor a : left3) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
        }
        if (!colliding) {
            for (Actor a : right1) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
        }
        if (!colliding) {
            for (Actor a : right3) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
            }
        }
        return colliding;
    }

    public boolean spaceAdjacent() {
        int z = facing ? 1 : -1;
        List<Actor> pose = getObjectsAtOffset((getImage().getWidth() / 2 + 2)*z, -getImage().getHeight() / 2 - 1, null);

        for (Actor a : pose) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                return false;
            }
        }

        return true;
    }
    
    public Actor upcomingWalkable(boolean facing, int floor) {
        int sign = facing ? 1 : -1;
        Object n = getOneObjectAtOffset(sign * (getImage().getWidth() / 2 + 7), (getImage().getHeight() / 2 + floor + 2), null);

        return (Actor) n;
    }

    public boolean touchingCeil() {
        return isColliding(0, -getImage().getHeight() / 2, null);
    }

    public int atWall()
    {
        if (getX() < (getImage().getWidth()/2+2))
        {
            return -1;
        } else if (getX() > (getWorld().getWidth()-(getImage().getWidth()/2))-2)
        {
            return 1;
        }
        return 0;
    }
    
    public boolean fall() {
        if (submerged() && !onGroundExclusive()) {
            if (!hasDrowned) {
                Audio.playSound("water.wav");
            }
            hasDrowned = true;
            setLocation(getX(), getY()+1);
            return true;
        }
        vSpeed += acceleration;
        int dir = (int) Math.signum(vSpeed);
        for (int step = 0; step != vSpeed; step += dir) {
            setLocation(getX(), getY() + dir);
            List<Actor> actors = getIntersectingObjects(null);
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    setLocation(getX(), getY() - dir);
                    if (Math.abs(vSpeed) >= 13) {
                        Audio.playSound("land.wav");
                    }
                    vSpeed = 0;
                    if (!onGroundExclusive()) {
                        headBoltTimer = 20;
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean fallNoColl() {
        vSpeed += acceleration;
        int dir = (int) Math.signum(vSpeed);
        for (int step = 0; step != vSpeed; step += dir) {
            setLocation(getX(), getY() + dir);
        }
        return true;
    }
    
    public int distFromFloor() {
        for (int i=0;i<22;i++) {
            Object n = getOneObjectAtOffset(0, getImage().getHeight() / 2 + (i*5) + 1, null);
            if (n instanceof Tile) {
                return (i*5);
            }
        }
        return (Integer.MAX_VALUE);
    }

    public boolean atBottom() {
        return getY() >= getWorld().getHeight() + (getImage().getHeight() / 2);
    }

    private void gameEnd() {
        Greenfoot.stop();
    }
    
    public void setVSpeed(int speed)
    {
        vSpeed = speed;
    }

}
