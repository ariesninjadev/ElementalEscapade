import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

enum Direction {
    RIGHT, DOWN, LEFT, UP;

    public Direction next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction) {
        switch (direction) {
            case RIGHT: x++; break;
            case DOWN: y++; break;
            case LEFT: x--; break;
            case UP: y--; break;
        }
    }
}

class Zone {
    int x;
    int y;
    int height;
    int width;
    ArrayList<Tile> tiles;
    
    public Zone(int x, int y, int height, int width, ArrayList<Tile> tiles) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.tiles = tiles;
    }
}

public class Game extends World
{

    Player me;
    Partner you;
    
    Static pressE;
    
    boolean gameIsMultiplayer = false;
    
    TextEngine text = new TextEngine(this);
    StaticEngine stat = new StaticEngine(this);
    
    int stage = 0;
    
    int playerNum = 0;
    
    Orb orb;
    int keyReq = 0;
    
    boolean reviving = false;
    
    // Lobby
    boolean inRange = false;
    boolean arrowShown = false;
    
    // Fade Transition
    int fadeStage = 0;
    BlackOverlay[] fadeStorage = new BlackOverlay[96];
    int step = 0;
    Direction direction = Direction.RIGHT;
    Point current = new Point(0, 0);
    int[][] bounds = {{12, 9}, {-2, -1}};
    public boolean noAnimate = false;
    
    // Audio
    public GreenfootSound gameMusic = new GreenfootSound("earth-bgm.wav");
    private int cVol = 36;
    private boolean canLoopDown = false;
    
    // Event Regions
    public ArrayList<Zone> zones = new ArrayList<>();
    
    
    public Game(boolean mp) throws java.io.IOException
    {    
        super(600, 400, 1, false); 
        
        gameIsMultiplayer = mp;
        
        lobby();

        new java.util.Timer().schedule( 
            new java.util.TimerTask() 
            {
                @Override
                public void run() {
                    gameMusic.playLoop();
                    cVol = 36;
                    gameMusic.setVolume(36);
                }
            }, 
            500
        );
        

    }
    
    public void keyObtained() {
        keyReq--;
        if (keyReq == 0) {
            new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Orb.locked = false;
                        Greenfoot.playSound("portal-unlocked.wav");
                    }
                }, 
                900
            );
        }
    }
    
    public void act() {
        
        System.out.println(cVol);
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null) {
            showText(mouse.getX() + ", " + mouse.getY(), 50, 20);
        }
        
        switch(stage) {
            
            case 0: // LOBBY
                try {
                    if (you.getY() != 1000 && playerNum == 1 && !arrowShown) {
                        //System.out.println(you.getY());
                        stat.show("darrow.png",550,290,"hover");
                        arrowShown = true;
                    }
                } catch (Exception e) {}
                break;
                
            default:
                break;
                
        }
        
        for (Zone zone : zones) {
            if (me.getX() > zone.x && 
                me.getX() < zone.x+zone.width && 
                me.getY() > zone.y &&  
                me.getY() < zone.y+zone.height) 
            {
                for (Tile tile : zone.tiles) {
                    tile.event();
                }
            }
        }
        
        if (me.getX() > orb.getX()-25 && 
            me.getX() < orb.getX()+25 && 
            me.getY() > orb.getY()-25 &&  
            me.getY() < orb.getY()+25 && 
            arrowShown && 
            keyReq == 0)
        {
            if (!inRange) {
                pressE = stat.show("press-e.png",534,10);
                inRange = true;
            }
        } else if (inRange) {
            removeObject(pressE);
            pressE = null;
            inRange = false;
        }
        if (Greenfoot.isKeyDown("e") && inRange && arrowShown && !Orb.locked) {
            Orb.locked = true;
            me.movementLocked = true;
            cVol = 0;
            gameMusic.setVolume(0);
            canLoopDown = true;
            Greenfoot.playSound("level-up.wav");
            stage++;
            noAnimate = true;
            fadeStage = 1;
        }

        if (fadeStage == 1 && step < 96) {
            Greenfoot.setSpeed( 56 );
            fadeOut();
        } else if (fadeStage == 1 && step == 96) {
            step--;
            unload();
            bootLevel(stage);
            fadeStage = 2;
        } else if (fadeStage == 2 && step == -1) {
            fadeStage = 0;
            step = 0;
            noAnimate = false;
            me.movementLocked = false;
            me.cancelIdle();
            resetFade();
            canLoopDown = false;
            //gameMusic.setVolume(50);
            //cVol = 50;
            if (reviving) {
                //System.out.println("asd");
                reviving = false;
                //gameMusic.playLoop();
                me.dead = false;
                me.hasDrowned = false;
            }
        } else if (fadeStage == 2 && step < 96) {
            Greenfoot.setSpeed( 56 );
            fadeIn();
        } else {
            Greenfoot.setSpeed( 47 );
        }
        if (canLoopDown && cVol >= 0) {
            cVol -= 2;
            gameMusic.setVolume(cVol);
        } else if (cVol < 36) {
            cVol += 2;
            gameMusic.setVolume(cVol);
        }
    }
    
    // LEVELS
    
    public void lobby() throws java.io.IOException {
        GreenfootImage background = getBackground();
        background.setColor(new Color(212,245,255));
        background.fillRect(0,0,getWidth(),getHeight());
        
        loadWorld(Data.lobby);
        
        me = new Player();
        addObject(me, 0, 1000);
        
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
        
        Orb.locked = false;
        
        setPaintOrder(BlackOverlay.class,GrassBlade.class,Player.class);
        
        if (gameIsMultiplayer) {
            
            you = new Partner();
            addObject(you, 0, 1000);
            String input = Greenfoot.ask("Game to join");
            String response = Network.joinGame(input);
            
            if (response.equals("-1")) {
                text.show("Can't connect to the server!", 300, 20, Color.ORANGE, new Color(0,0,0), 24);
                Greenfoot.stop();
                return;
            } else if (response.equals("0")) {
                text.show("This game is full!", 300, 20, Color.ORANGE, new Color(0,0,0), 24);
                Greenfoot.stop();
                return;
            } else if (response.equals("1")) {
                text.show(" Joined as player 1. ", 300, 20, Color.RED, new Color(0,0,0), 24);
                me.init("player");
                you.init("partner");
                me.setLocation(280, 200);
                playerNum = 1;
                me.startLocalPost();
                you.partnerGet();
            } else if (response.equals("2")) {
                text.show(" Joined as player 2. ", 300, 20, Color.BLUE, new Color(0,0,0), 24);
                me.init("partner");
                you.init("player");
                me.setLocation(320, 200);
                playerNum = 2;
                me.startLocalPost();
                you.partnerGet();
            } else {
                text.show("There was an error.", 300, 20, Color.ORANGE, new Color(0,0,0), 24);
            }
            
        } else {
            me.setLocation(300, 200);
            stat.show("darrow.png",550,290,"hover");
            arrowShown = true;
        }
    }
    
    public void earth1() {
        loadWorld(Data.earth1);
        keyReq = 3;
        me.waterProtected = false;
        recastPlayer(30,320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
    }
    
    public void earth2() {
        loadWorld(Data.earth2);
        addObject(new SmallEnemy("sand-snake.png"),230,30);
        keyReq = 3;
        me.waterProtected = false;
        recastPlayer(30,320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
        
        // Rocks
        ArrayList<Tile> group1 = new ArrayList<>();
        ArrayList<Tile> group2 = new ArrayList<>();
        ArrayList<Tile> group3 = new ArrayList<>();
        List<RocksHanging> allRocks = getObjects(RocksHanging.class);
        for (RocksHanging rock : allRocks) {
            if (rock.getX() > 410) {
                group1.add((Tile)rock);
            } else if (rock.getX() > 330) {
                group2.add((Tile)rock);
            } else {
                group3.add((Tile)rock);
            }
        }
        Zone zone1 = new Zone(360, 280, 80, 40, group1);
        Zone zone2 = new Zone(380, 100, 140, 25, group2);
        Zone zone3 = new Zone(250, 100, 80, 30, group3);
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
    }
    
    // -------------------------------------------------- //
    
    public void bootLevel(int lvl) {
        Key.v = -1;
        switch (lvl) {
            case 1:
                earth2();
                break;
            case 2:
                earth2();
                break;
        }
    }
    
    public void restartLevel() {
        //gameMusic.stop();
        canLoopDown = true;
        Orb.locked = true;
        keyReq = 3;
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    Greenfoot.playSound("lose.wav"); 
                    new java.util.Timer().schedule( 
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                reviving = true;
                                fadeStage = 1;
                                me.movementLocked = true;
                            }
                        }, 
                        700 
                    );
                }
            }, 
            1000 
        );
    }
    
    public void recastPlayer(int x, int y) {
        if (gameIsMultiplayer) {
            int z = (playerNum==1) ? -1 : 1;
            me.setLocation(x+(16*z), y);
        } else {
            me.setLocation(x, y);
        }
    }
    
    public void loadWorld(int[][] num) {
        for (int i=0;i<num.length;i++)
        {
            for (int g=0;g<num[i].length;g++)
            {
                int x=g*20+10;
                int y=i*20+10;
                Tile tile = TileMap.getTile(num[i][g]);
                if (tile!=null)
                {
                    addObject(tile,x,y);
                }
            }
        }
    }
    
    public void stopped() {
        try {
        me.stopLocalPost();
        you.stopGet();
        } catch (Exception e) {}
    }
    
    public void unload() {
        List<Tile> u1 = getObjects(Tile.class);
        List<Text> u2 = getObjects(Text.class);
        List<Static> u3 = getObjects(Static.class);
        List<SmallEnemy> u4 = getObjects(SmallEnemy.class);
        List<Actor> objects = new ArrayList<>();
        objects.addAll(u1);
        objects.addAll(u2);
        objects.addAll(u3);
        objects.addAll(u4);
        removeObjects(objects);
    }
    
    public void fadeOut() {
        fadeStorage[step] = new BlackOverlay();
        addObject(fadeStorage[step], current.x*50+25, current.y*50+25);
        if (reachedBoundary()) {
            direction = direction.next();
            updateBounds();
        }
        current.move(direction);
        step++;
    }

    public void fadeIn() {
        if (step >= 0) {
            removeObject(fadeStorage[95-step]);
            fadeStorage[95-step] = null;
            step--;
        }
    }
    
    private void resetFade() {
        fadeStorage = new BlackOverlay[96];
        step = 0;
        direction = Direction.RIGHT;
        current = new Point(0, 0);
        bounds = new int[][]{{12, 9}, {-2, -1}};
    }
    
    private boolean reachedBoundary() {
        switch (direction) {
            case RIGHT: return current.x+1 == bounds[0][0];
            case DOWN: return current.y+1 == bounds[0][1];
            case LEFT: return current.x-1 == bounds[1][0];
            case UP: return current.y-1 == bounds[1][1];
        }
        return false;
    }
    
    private void updateBounds() {
        switch (direction) {
            case RIGHT: bounds[0][0]--; break;
            case DOWN: bounds[0][1]--; break;
            case LEFT: bounds[1][0]++; break;
            case UP: bounds[1][1]++; break;
        }
    }
}
