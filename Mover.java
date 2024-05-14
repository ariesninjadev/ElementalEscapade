    import greenfoot.*;

/**
 * The class Mover provides some basic movement methods. Use this as a superclass
 * for other actors that should be able to move left and right, jump up and fall 
 * down.
 */
public class Mover extends Actor
{
    private static final int acceleration = 1;      // down (gravity)
    public static final int speed = 7;             // running speed (sideways)
    
    protected int vSpeed = 0;                         // current vertical speed
    

    public void moveRight()
    {
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
        vSpeed+=1; // add gravity
        int dir=(int)Math.signum(vSpeed); // determine direction
        for(int step=0; step!=vSpeed; step+=dir) // for each pixel-step
        {
            setLocation(getX(), getY()+dir); // move 
            if(getOneIntersectingObject(null)!=null) // check intersection
            {
                setLocation(getX(), getY()-dir); // resistance (step-back)
                vSpeed=0; // stopped
                break; // forces exit out of 'for' loop
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
