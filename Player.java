import greenfoot.*;

/**
 * A little penguin that wants to get to the other side.
 */
public class Player extends Mover
{
    private String keyForUp = null;
    private boolean upPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    public boolean sprinting = false;
    public String sprintKey = null;
    private int sprintClock = 0;
    
    private GreenfootImage image;
    
    Thread thread;
    
    private boolean facing = true;
    private boolean facingProc = true;
    public static final int speed = 2;
    private boolean idle = true;
    private int idleClock = 0;
    private boolean idleAnim = false;
    private Actor uw;
    
    
    private String activeCostume = "player-still.png";
    
    public String whoAmI = "player";
    
    public void act() 
    {
        Greenfoot.setSpeed( 47 );
        
        //System.out.println(distFromFloor());
        
        uw = upcomingWalkable(facing,distFromFloor());
        idleClock++;
        
        if (sprintClock > 0) {
            sprintClock--;
        }
        if (headBoltTimer > 0) {
            headBoltTimer--;
        } 
      
        checkKeys();  
        
        if (vSpeed >= 0) {
            checkFall();
        } else {
            checkRoof();
        }
        if (!onGroundExclusive() && this.jumpCount == 0) {
            this.jumpCount = 1;
        }
        if (idle && idleClock > 120 && !idleAnim) {
            idleAnim = true;
            switchCostume(whoAmI+"-idle.png");
            orient(facing);
            int s = facing ? -1 : 1;
            setLocation(getX()-(2*s),getY()-6);
        } else if (!idle && idleAnim) {
            idleAnim = false;
            switchCostume(whoAmI+"-still.png");
            orient(facing);
            int s = facing ? -1 : 1;
            setLocation(getX()+(2*s),getY()+6);
        }
        if (facing != facingProc) {
            orient(facing);
            facingProc = facing;
        }
    }
    
    private void checkKeys()
    {
        if (Greenfoot.isKeyDown("d") && atWall() != 1) {
            facing = true;
            if (! (uw instanceof Wave)) {
                if (!leftPressed && sprintClock > 0 && sprintKey == "d") {
                    sprinting = true;
                }
                sprintKey = "d";
                if (!leftPressed) {
                    sprintClock = 11;
                }
                idle = false;
                idleClock = 0;
                leftPressed = true;
                moveRight(speed, sprinting);
            }
        }
        if (Greenfoot.isKeyDown("a") && atWall() != -1) {
            facing = false;
            if (! (uw instanceof Wave)) {
                if (!rightPressed && sprintClock > 0 && sprintKey == "a") {
                    sprinting = true;
                }
                sprintKey = "a";
                if (!rightPressed) {
                    sprintClock = 11;
                }
                rightPressed = true;
                idle = false;
                idleClock = 0;
                moveLeft(speed, sprinting);
            }
        }
        if (!Greenfoot.isKeyDown("a")) {
            rightPressed = false;
        }
        if (!Greenfoot.isKeyDown("d")) {
            leftPressed = false;
        }
        if (sprintKey == null || !Greenfoot.isKeyDown(sprintKey)) {
            sprinting = false;
        }
        if (Greenfoot.isKeyDown("space"))
        {
            if (upPressed || headBoltTimer > 0) {
                return;
            }
            idle = false;
            idleClock = 0;
            upPressed = true;
            if (this.jumpCount <= 1) {
                this.jumpCount++;
                jump();
            }
        }
        if (!Greenfoot.isKeyDown("space"))
        {
            upPressed = false;
        }
        if (!Greenfoot.isKeyDown("space") && 
        !Greenfoot.isKeyDown("a") && 
        !Greenfoot.isKeyDown("d") &&
        onGroundExclusive())
        {
            idle = true;
        }
    }    
    
    private void checkFall()
    {
        if (onGround()) {
            setVSpeed(0);
            this.jumpCount = 0;
            upPressed = false;
        }
        else {
            if (!fall()) { // This will execute the fall method AND check it at the same time.
                this.jumpCount = 0;
                upPressed = false;
            }
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
    
    public Player() {
        int sizer = 2;
        GreenfootImage img = new GreenfootImage(this.getImage());
        img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
        setImage(img);
    }
    
    public void switchCostume(String costume) {
        activeCostume = costume;
        setImage(costume);
        GreenfootImage img = getImage();
        int sizer = 2;
        img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
        //orient(facing);
        setImage(img);
    }
    
    public void orient(boolean dir) {
        setImage(activeCostume);
        GreenfootImage img = getImage();
        int sizer = 2;
        img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
        if (!dir) {
            img.mirrorHorizontally();
        }
        setImage(img);
    }
}

    