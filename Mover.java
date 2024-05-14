    import greenfoot.*;

/**
 * The class Mover provides some basic movement methods. Use this as a superclass
 * for other actors that should be able to move left and right, jump up and fall 
 * down.
 */
public class Mover extends Actor
{
    private static final int acceleration = 1;      // down (gravity)
    public static final int speed = 3;             // running speed (sideways)
    
    protected int vSpeed = 0;                         // current vertical speed
    

    public void moveRight()
    {
        for(int step=0; step<=speed; step+=1) // For each pixel
        {
            setLocation(getX()+1, getY()); // Update Location
            if(atSide()) // Check Intersection
            {
                setLocation(getX()-1, getY()); // Move the character back if touching
                break; // Stop the rest of the check
            }
        }
        setLocation ( getX() + speed, getY() );
    }
    
    public void moveLeft()
    {
        setLocation ( getX() - speed, getY() );
    }
    
    public boolean onGround()
    {
        Object under = getOneObjectAtOffset(0, getImage().getHeight()/2 , null);
        if (under instanceof Partner) {
            return false;
        }
        return under != null;
    }
    
    public boolean onGroundExclusive()
    {
        Object under1 = getOneObjectAtOffset(-getImage().getHeight()/2, getImage().getHeight()/2 , null);
        Object under2 = getOneObjectAtOffset(0, getImage().getHeight()/2 , null);
        Object under3 = getOneObjectAtOffset(getImage().getHeight()/2, getImage().getHeight()/2 , null);
        if (under1 instanceof Partner || under2 instanceof Partner || under3 instanceof Partner) {
            return false;
        }
        return (under1 != null) || (under2 != null) || (under3 != null);
    }
    
    public boolean atSide()
    {
        Object left1 = getOneObjectAtOffset(-getImage().getHeight()/2, getImage().getHeight()/2-2 , null);
        Object left2 = getOneObjectAtOffset(-getImage().getHeight()/2, 0 , null);
        Object left3 = getOneObjectAtOffset(-getImage().getHeight()/2, -getImage().getHeight()/2+2 , null);
        Object right1 = getOneObjectAtOffset(getImage().getHeight()/2, getImage().getHeight()/2-2 , null);
        Object right2 = getOneObjectAtOffset(getImage().getHeight()/2, 0 , null);
        Object right3 = getOneObjectAtOffset(getImage().getHeight()/2, -getImage().getHeight()/2+2 , null);
        if (left1 instanceof Partner || left2 instanceof Partner || left3 instanceof Partner
         || right1 instanceof Partner || right2 instanceof Partner || right3 instanceof Partner) {
            return false;
        }
        return (left1 != null) || (left2 != null) || (left3 != null) || (right1 != null) || (right2 != null) || (right3 != null);
    }
    
    public boolean touchingCeil()
    {
        Object above = getOneObjectAtOffset(0, -getImage().getHeight()/2 , null);
        if (above instanceof Partner) {
            return false;
        }
        return above != null;
    }
    
    public void setVSpeed(int speed)
    {
        vSpeed = speed;
    }
    
    public void fall()
    {
        vSpeed += acceleration; // Modify Speed
        int dir=(int)Math.signum(vSpeed); // Get Direction
        for(int step=0; step!=vSpeed; step+=dir) // For each pixel
        {
            setLocation(getX(), getY() + dir); // Update Location
            if(getOneIntersectingObject(null)!=null) // Check Intersection
            {
                setLocation(getX(), getY()-dir); // Move the character back if touching
                vSpeed=0; // Cancel Vertical Momentum
                break; // Stop the rest of the check
            }
        }
    }
    
    private boolean atBottom()
    {
        return getY() >= getWorld().getHeight() - 2;
    }
    
    private void gameEnd()
    {
        Greenfoot.stop();
    }


}
