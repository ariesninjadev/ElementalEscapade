import greenfoot.*;

/**
 * A little penguin that wants to get to the other side.
 */
public class Player extends Mover
{

    public static final int speed = 2; // Player speed

    public String whoAmI = "player";

    // Key states
    private boolean upPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Sprint States
    public boolean sprinting = false;
    public String sprintKey = null; // Which direction are we sprinting in?
    private int sprintClock = 0; // Timer to check the time between presses of a key

    // Idle States
    private boolean idle = true;
    private int idleClock = 0;
    private boolean idleAnim = false;
    public boolean dead = false;

    // Visual
    private String activeCostume = "player-still.png"; // Current costume link
    private GreenfootImage image; // Current costume
    private boolean facingProc = true; // Which direction is our costume facing?

    // Network
    Thread thread; // Isolated environment for multiplayer features

    // Misc
    private boolean done = false; // Is the player finished with the current scene?
    private Actor uw; // The next tile the player would land on if they were grounded (used for waterborder)
    public boolean waterProtected = true; // Are we prevented from falling into water?
    private int epicenter = 0; // Some costumes require us to recenter the player
    public boolean movementLocked = false;
    private int walkAudioFileClock = 0; // Clock to regulate the frequency of a walk sound playing

    public int health=100;
    public Player() {
        orient(true); // Update costume size and direction on start
    }

    public void init(String who) {
        setImage(who + "-still.png");
        orient(true);
        whoAmI = who;
    }

    // 20ms delayed loop
    public void act() 
    {

        uw = upcomingWalkable(facing,distFromFloor()); // Update lookahead

        //Update clocks
        idleClock++;
        walkAudioFileClock++;
        if (sprintClock > 0) {
            sprintClock--;
        }
        if (headBoltTimer > 0) {
            headBoltTimer--;
        } 

        checkKeys(); // Check all keypresses

        // Watch for a floor if we are falling or a roof if we are rising
        if (vSpeed >= 0) {
            checkFall();
        } else {
            checkRoof();
        }

        // If we fell without jumping, prevent the player from double jumping
        if (!onGroundExclusive() && this.jumpCount == 0) {
            this.jumpCount = 1;
        }

        // Switch to the idle animation after 120 frames of inactivity
        if (idle && idleClock > 120 && !idleAnim) {
            idleAnim = true;
            switchCostume(whoAmI+"-idle.png");
            Greenfoot.playSound("pickup-torch.wav");
            orient(facing);
            int s = facing ? -1 : 1; // Convert direction boolean to -1 or 1
            epicenter = -3;
            setLocation(getX()-(2*s),getY()-4); // Account for the difference in size
        } else if (!idle && idleAnim) {
            idleAnim = false;
            switchCostume(whoAmI+"-still.png");
            Greenfoot.playSound("put-away-torch.wav");
            orient(facing);
            int s = facing ? -1 : 1;
            epicenter = 0;
            setLocation(getX()+(2*s),getY()+4);
        }

        // If our direction and costume orientation disagree, update the costume
        if (facing != facingProc) {
            orient(facing);
            facingProc = facing;
        }

        if (!dead && atBottom()) {
            dead = true;
            ((Game)getWorld()).restartLevel(); 
        }
    }

    // Scan for keypresses
    private void checkKeys()
    {
        // Don't check keys on animations, cutscenes, etc.
        if (movementLocked) {
            return;
        }

        // Proceed if not at wall and d key pressed
        if (Greenfoot.isKeyDown("d") && atWall() != 1) {
            facing = true; // We are now facing right

            // Proceed if the next tile isn't water or if we arent protected
            if (!(uw instanceof Wave) || !waterProtected) {

                // If we JUST PRESSED the d key
                if (!rightPressed) {

                    // If we pressed d less than 11 frames ago, sprint
                    if (sprintClock > 0 && sprintKey == "d" && !sprinting) {
                        sprinting = true;
                        Greenfoot.playSound("sprint.wav");
                    }
                    sprintKey = "d"; // If we start sprinting, it's gonna be to the right!
                    sprintClock = 11; // Reset the sprint clock
                }

                rightPressed = true; // The D key is now held until noted otherwise

                idle = false; // We are no longer idle!
                idleClock = 0;

                playWalk(); // Play a walking sound

                moveRight(speed, sprinting); // Move the player to the right
            }
        }

        // Identical function for left
        if (Greenfoot.isKeyDown("a") && atWall() != -1) {
            facing = false;
            if (!(uw instanceof Wave) || !waterProtected) {
                if (!leftPressed) {
                    if (sprintClock > 0 && sprintKey == "a" && !sprinting) {
                        sprinting = true;
                        Greenfoot.playSound("sprint.wav");
                    }
                    sprintKey = "a";
                    sprintClock = 11;
                }
                leftPressed = true;
                idle = false;
                idleClock = 0;
                playWalk();
                moveLeft(speed, sprinting);
            }
        }

        // Update keystates
        if (!Greenfoot.isKeyDown("a")) {
            leftPressed = false;
        }
        if (!Greenfoot.isKeyDown("d")) {
            rightPressed = false;
        }

        // If the key we used to initialize a sprint is now up, stop sprinting
        if (sprintKey == null || !Greenfoot.isKeyDown(sprintKey)) {
            sprinting = false;
        }

        // If the space key is pressed
        if (Greenfoot.isKeyDown("space"))
        {
            // If space is already pressed our we hit our head too recently (headbolt/concussion), cancel
            if (upPressed || headBoltTimer > 0) {
                return;
            }

            idle = false;
            idleClock = 0;
            upPressed = true;

            // If we have jumped zero or one times, jump
            if (this.jumpCount <= 1) {
                if (this.jumpCount == 0) {
                    Greenfoot.playSound("jump.mp3");
                } else {
                    Greenfoot.playSound("jump.mp3");                
                }
                this.jumpCount++;
                jump();
            }
        }

        // Keystate
        if (!Greenfoot.isKeyDown("space"))
        {
            upPressed = false;
        }

        // We are idle if no critical keys are pressed
        if (!Greenfoot.isKeyDown("space") && 
        !Greenfoot.isKeyDown("a") && 
        !Greenfoot.isKeyDown("d") &&
        onGroundExclusive())
        {
            idle = true;
        }
    }    

    // Check if we are currently falling
    private void checkFall()
    {
        // If on ground, cancel all velocity and update states
        if (onGround() && !submerged()) {
            setVSpeed(0);
            this.jumpCount = 0;
            upPressed = false;
        } else {
            boolean fallResult = fall(); // This will execute the fall method AND check it at the same time

            // Fall returns true if we are still falling. This updates states if we have landed
            if (!fallResult) {
                this.jumpCount = 0;
                upPressed = false;
                //walkAudioFileClock = Integer.MAX_VALUE; // Force a walk sound to play when we land
            }
        }
    }

    // Check if touching the roof
    private void checkRoof()
    {
        // Cancels momentum if we hit a ceiling
        if (touchingCeil()) {
            setVSpeed(0);
        } else {
            fall();
        }
    }

    // Switch the players' costume
    public void switchCostume(String costume) {
        activeCostume = costume; // Update state
        orient(facing); // Call orient
    }

    // Mirror and scale the costume on demand
    public void orient(boolean dir) {
        setImage(activeCostume);
        GreenfootImage img = getImage();
        int sizer = -2;
        img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10); // Upscales the costume by 2%

        // If we are facing left, mirror the image
        if (!dir) {
            img.mirrorHorizontally();
        }

        setImage(img);
    }

    // Allows the world to cancel your idleness
    public void cancelIdle() {
        idle = false;
        idleClock = 0;
    }

    // Play the necessary walking audio file
    public void playWalk() {
        int gravity = sprinting ? 2 : 3;
        if (!exponentialNoise(walkAudioFileClock, gravity, 8)) {
            return;
        }
        walkAudioFileClock = 0;
        Tile on = standingOn();
        if (on != null && on.walkFile() != null) {
            Greenfoot.playSound(on.walkFile()); 
        }
    }

    public boolean exponentialNoise(int num, int gravity, int hardstop) {
        int threshold = 12;
        for (int i=0;i<hardstop;i++) {
            if (Greenfoot.getRandomNumber(gravity)==0) {
                threshold--;
            }
        }
        return num >= threshold;
    }

    // Begin posting our location to the network if on multiplayer.
    // DOCUMENTATION FOR NET NOT PROVIDED
    public void startLocalPost()
    {
        thread = new Thread(new Runnable() 
            {
                public void run() 
                {
                    while (true) {
                        try
                        {
                            Network.postLocation(getX(),getY(), done);
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

    public void hit(int damage)
    {
        health-=damage;
        if (health<=0)
        {
            Game game=(Game) getWorld();
            game.restartLevel();
            health=100;
        }
    }
}
 