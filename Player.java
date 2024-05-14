import greenfoot.*;

/**
 * A little penguin that wants to get to the other side.
 */
public class Player extends Mover
{
    private static final int jumpStrength = 16;
    
    Thread thread;
    
    public void act() 
    {
        checkKeys();  
        System.out.println(vSpeed);
        if (vSpeed >= 0) {
            checkFall();
        } else {
            checkRoof();
        }
    }
    
    private void checkKeys()
    {
        if (Greenfoot.isKeyDown("left") && atWall() != -1) {
            moveLeft();
        }
        if (Greenfoot.isKeyDown("right") && atWall() != 1) {
            moveRight();
        }
        if (Greenfoot.isKeyDown("up") )
        {
            if (onGroundExclusive())
                jump();
        }
    }    
    
    private void jump()
    {
        setVSpeed(-jumpStrength);
        fall();
    }
    
    private void checkFall()
    {
        if (onGround()) {
            setVSpeed(0);
        }
        else {
            fall();
        }
    }
    
    private void checkRoof()
    {
        if (touchingCeil()) {
            setVSpeed(0);
        } else {
            fall();
        }
    }
    
    private int atWall()
    {
        if (getX() < (getImage().getWidth()/2))
        {
            return -1;
        } else if (getX() > getWorld().getWidth()-(getImage().getWidth()/2))
        {
            return 1;
        }
        return 0;
    }
    
    public void startLocalPost()
    {
        thread = new Thread(new Runnable() 
        {
            public void run() 
            {
                while (true) {
                    try
                    {
                        Network.postLocation(getX(),getY());
                    }
                    catch (java.io.IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    
    public void stopLocalPost()
    {
        try {
            thread.stop();
        } catch (java.lang.NullPointerException npe) {}
    }
}

    