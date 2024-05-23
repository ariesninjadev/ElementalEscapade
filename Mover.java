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

    protected int vSpeed = 0;

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

    public void moveRight(int speed, boolean sprinting) {
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
                if (jumpCount == 0) {
                    jump();
                    jumpCount++;
                }
                return;
            }
        }
    }

    public void moveLeft(int speed, boolean sprinting) {
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
                if (jumpCount == 0) {
                    jump();
                    jumpCount++;
                }
                return;
            }
        }
    }

    public boolean onGround() {
        return isColliding(0, getImage().getHeight() / 2, null);
    }

    public boolean onGroundExclusive() {
        List<Actor> under1 = getObjectsAtOffset(-getImage().getWidth() / 2, getImage().getHeight() / 2 + 1, null);
        List<Actor> under2 = getObjectsAtOffset(0, getImage().getHeight() / 2 + 1, null);
        List<Actor> under3 = getObjectsAtOffset(getImage().getWidth() / 2, getImage().getHeight() / 2 + 1, null);
        boolean colliding = false;
        for (Actor a : under1) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                colliding = true;
                break;
            }
        }
        if (!colliding) {
            for (Actor a : under2) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
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

    public boolean atSide() {
        List<Actor> left1 = getObjectsAtOffset(-getImage().getWidth() / 2, getImage().getHeight() / 2 - 1, null);
        List<Actor> left2 = getObjectsAtOffset(-getImage().getWidth() / 2, 0, null);
        List<Actor> left3 = getObjectsAtOffset(-getImage().getWidth() / 2, -getImage().getHeight() / 2 + 1, null);
        List<Actor> right1 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, getImage().getHeight() / 2 - 1, null);
        List<Actor> right2 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, 0, null);
        List<Actor> right3 = getObjectsAtOffset(getImage().getWidth() / 2 - 1, -getImage().getHeight() / 2 + 1, null);
    
        boolean colliding = false;
        for (Actor a : left1) {
            if (a instanceof Tile && ((Tile) a).isCollidable()) {
                colliding = true;
                break;
            }
        }
        if (!colliding) {
            for (Actor a : left2) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    colliding = true;
                    break;
                }
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
            for (Actor a : right2) {
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

    public boolean spaceUpLeft() {
        Object n = getOneObjectAtOffset(-getImage().getWidth() / 2 - 1, (-getImage().getWidth() / 2 - 1), null);

        if (n instanceof Partner) {
            return true;
        }

        if (n instanceof Tile && !((Tile) n).isCollidable()) {
            return true;
        }

        return n == null;
    }
    
    public Actor upcomingWalkable(boolean facing) {
        int sign = facing ? 1 : -1;
        Object n = getOneObjectAtOffset(sign * (getImage().getWidth() / 2 + 4), (getImage().getHeight() / 2 + 3), null);
        
        // System.out.println(n);

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
        vSpeed += acceleration;
        int dir = (int) Math.signum(vSpeed);
        for (int step = 0; step != vSpeed; step += dir) {
            setLocation(getX(), getY() + dir);
            List<Actor> actors = getIntersectingObjects(null);
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable()) {
                    setLocation(getX(), getY() - dir);
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

    private boolean atBottom() {
        return getY() >= getWorld().getHeight() - 2;
    }

    private void gameEnd() {
        Greenfoot.stop();
    }
    
    public void setVSpeed(int speed)
    {
        vSpeed = speed;
    }

}
