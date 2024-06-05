import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Boss here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Boss extends Mover {
    /**
     * Act - do whatever the Boss wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private boolean direction = true;
    private boolean spriteFacing = false;
    private GreenfootImage base;
    private String monster;
    public boolean attacking = false;
    private boolean attackReady = false;
    private int attackDelay = -100;
    private int idleDelay = 0;
    private int framesDelay = 0;
    private int attackframes = 0;
    private double speed = 0.4;
    public boolean predictedContact = false;
    public int health = 3;
    private double ac = 0;
    private boolean invulnerable = false;
    private int invulnerableClock = 0;
    private boolean recognizeGround = true;

    public Boss(String name) {
        base = new GreenfootImage(name);
        monster = name;
        setImage(base);
        // GreenfootImage img = getImage();
        // int sizer = 5;
        // img.scale(img.getWidth()*(10+(++sizer))/10, img.getHeight()*(10+sizer)/10);
        // // Upscales the costume by 2%
        // setImage(img);
    }

    public void act() {
        // System.out.println(attackframes);
        idleAnimation();
        // fit();
        checkAttacking();
        checkAttack();
        attackDelay++;
        idleDelay++;
        move();
        checkFall();
        predictContact();
        if (invulnerableClock > 0) {
            invulnerable = true;
            invulnerableClock--;
        } else {
            invulnerable = false;
        }
    }

    public void move() {
        if (((Game) getWorld()).noAnimate || attacking) {
            return;
        }
        if (speed < 1) {
            ac += speed;
            if (ac >= 1) {
                ac = 1.0;
            } else {
                return;
            }
        } else {
            ac = (float) speed;
        }
        if (direction) {
            boolean atWall = moveRight((int) ac, false);
            if (atWall) {
                // direction = !direction;
            }
        } else {
            boolean atWall = moveLeft((int) ac, false);
            if (atWall) {
                // direction = !direction;
            }
        }
        ac = 0;
        boolean modif = Math.abs(((Game) getWorld()).me.getX() - getX()) < 30;
        if ((((Game) getWorld()).me.getX() > getX()) != direction && !modif) {
            direction = !direction;
        }
        if (spriteFacing != direction) {
            spriteFacing = direction;
            GreenfootImage img = getImage();
            img.mirrorHorizontally();
            setImage(img);
        }
    }

    private void checkFall() {
        if (onGround() && recognizeGround) {
            setVSpeed(0);
            this.jumpCount = 0;
        } else {
            if (!fall()) {
                this.jumpCount = 0;
            }
        }
    }

    public boolean fall() {
        vSpeed += 1;
        int dir = (int) Math.signum(vSpeed);
        for (int step = 0; step != vSpeed; step += dir) {
            setLocation(getX(), getY() + dir);
            List<Actor> actors = getIntersectingObjects(null);
            for (Actor a : actors) {
                if (a instanceof Tile && ((Tile) a).isCollidable() && recognizeGround) {
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

    public void idleAnimation() {
        if (!attacking) {
            if (idleDelay == 5) {
                if (getImage() != base) {
                    setImage(base);
                } else if (getImage() == base) {
                    switch (monster) {
                        case "Wicked-worm-idle-1.png":
                            // setImage(new GreenfootImage("Wicked-worm-idle-2.png"));
                            break;
                        case "Super-squid-idle-1.png":
                            setImage(new GreenfootImage("Super-squid-idle-2.png"));
                            break;
                        case "Oscillating-fan-idle-1.png":
                            setImage(new GreenfootImage("Oscillating-fan-idle-2.png"));
                            break;
                    }
                }
                idleDelay = 0;
            }
        }
    }

    public void checkAttacking() {
        List<Player> players = getObjectsInRange(50, Player.class);
        if (!players.isEmpty() && attackDelay >= 100) {
            attackReady = true;
            attackDelay = -100;
        }
    }

    public void predictContact() {
        int modif = direction ? 1 : -1;
        List<Actor> actors = getObjectsAtOffset((getImage().getWidth() / 2) * modif, 4, null);
        for (Actor a : actors) {
            if (a instanceof Player) {
                predictedContact = true;
                return;
            }
        }
        List<Actor> actors2 = getObjectsAtOffset((getImage().getWidth() / 2 - 20) * modif, 4, null);
        for (Actor a : actors2) {
            if (a instanceof Player) {
                predictedContact = true;
                return;
            }
        }
        predictedContact = false;
        List<Actor> actors3 = getObjectsAtOffset((getImage().getWidth() / 2 - 34) * modif, -getImage().getHeight() / 2,
                null);
        for (Actor a : actors3) {
            if (a instanceof Player && !invulnerable && attacking) {
                takeDamage();
                return;
            }
        }
    }

    public void takeDamage() {
        invulnerableClock = 150;
        health--;
        speed += 0.6;
        speed = Math.round(speed);
        ((Game) getWorld()).me.launch();
        Audio.playSound("boss-hurt.wav");
        if (health == 0) {
            ((Game) getWorld()).endBoss();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            recognizeGround = false;
                        }
                    },
                    600);
        }
    }

    public void checkAttack() {
        if (attackReady && monster != "Oscillating-fan-idle-1" && recognizeGround) {
            // System.out.println(attackframes);
            switch (attackframes) {
                case 1:
                    Audio.playSound("boss-prep.wav");
                    switch (monster) {
                        case "Wicked-worm-idle-1.png":
                            switchCostume("Wicked-worm-attack-1.png");
                            break;
                        case "Super-squid-idle-1":
                            switchCostume("Super-squid-attack-1");
                            break;
                    }
                    break;
                case 70:
                    attacking = true;
                    Audio.playSound("boss-bite.wav");
                    switch (monster) {
                        case "Wicked-worm-idle-1.png":
                            switchCostume("Wicked-worm-attack-2.png");
                            break;
                        case "Super-squid-idle-1":
                            switchCostume("Super-squid-attack-2");
                            break;
                    }
                    break;
                case 130:
                    Audio.playSound("boss-prep.wav");
                    switchCostume("Wicked-worm-idle-1.png");
                    attacking = false;
                    attackReady = false;
                    attackframes = 0;
                    break;

            }
            attackframes++;
        }
    }

    public void switchCostume(String c) {
        setImage(c);
        GreenfootImage img = getImage();
        if (direction) {
            img.mirrorHorizontally();
        }
        setImage(img);
    }
}
