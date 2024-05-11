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
        checkFall();
    }
    
    private void checkKeys()
    {
        if (Greenfoot.isKeyDown("left") )
        {
            //setImage("pengu-left.png");
            moveLeft();
        }
        if (Greenfoot.isKeyDown("right") )
        {
            //setImage("pengu-right.png");
            moveRight();
        }
        if (Greenfoot.isKeyDown("up") )
        {
            if (onGround())
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
        thread.stop();
    }
}

    