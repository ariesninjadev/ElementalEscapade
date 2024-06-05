import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
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
            case RIGHT:
                x++;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case UP:
                y--;
                break;
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

public class Game extends World {

    private boolean DEBUG = false;

    public Player me;
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
    int[][] bounds = { { 12, 9 }, { -2, -1 } };
    public boolean noAnimate = false;

    // Audio
    public Audio gameMusic = new Audio("earth-bgm.wav");
    public Audio bossMusic = new Audio("air-bgm.wav");
    private int cVol = 36;
    private int cVolBoss = 0;
    private boolean canLoopDown = false;
    private boolean canLoopDownBoss = true;

    // Event Regions
    public ArrayList<Zone> zones = new ArrayList<>();
    
    // Text Builder
    private String endC = "";
    private String endFull = "Thanks for playing!";
    private int etClock = -250;

    public Game(boolean mp) throws java.io.IOException {
        super(600, 400, 1, false);

        gameIsMultiplayer = mp;

        lobby();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        gameMusic.playLoop();
                        cVol = 36;
                        gameMusic.setVolume(36);
                    }
                },
                500);

    }

    public void keyObtained() {
        keyReq--;
        if (keyReq == 0) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Orb.locked = false;
                            Audio.playSound("portal-unlocked.wav");
                        }
                    },
                    900);
        }
    }

    public void act() {

        MouseInfo mouse = Greenfoot.getMouseInfo();

        if (mouse != null && DEBUG) {
            showText(mouse.getX() + ", " + mouse.getY(), 50, 20);
        }

        switch (stage) {

            case 0: // LOBBY
                try {
                    if (you.getY() != 1000 && playerNum == 1 && !arrowShown) {
                        // System.out.println(you.getY());
                        stat.show("darrow.png", 550, 290, "hover");
                        arrowShown = true;
                    }
                } catch (Exception e) {
                }
                break;
            case 5:
                showText(endC, 300, 250);
                etClock++;
                if (etClock == 4 && endC.length() < endFull.length()) {
                    etClock = 0;
                    endC += endFull.charAt(endC.length());
                }
                break;
            default:
                break;

        }

        for (Zone zone : zones) {
            if (me.getX() > zone.x &&
                    me.getX() < zone.x + zone.width &&
                    me.getY() > zone.y &&
                    me.getY() < zone.y + zone.height) {
                for (Tile tile : zone.tiles) {
                    tile.event();
                }
            }
        }

        if (me.getX() > orb.getX() - 25 &&
                me.getX() < orb.getX() + 25 &&
                me.getY() > orb.getY() - 25 &&
                me.getY() < orb.getY() + 25 &&
                arrowShown &&
                keyReq == 0) {
            if (!inRange) {
                pressE = stat.show("press-e.png", 534, 10);
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
            canLoopDownBoss = true;
            Audio.playSound("level-up.wav");
            stage++;
            fadeStage = 1;
        }

        if (fadeStage == 1 && step < 96) {
            Greenfoot.setSpeed(56);
            fadeOut();
            noAnimate = true;
        } else if (fadeStage == 1 && step == 96) {
            step--;
            unload();
            if (reviving) {
                me.dead = false;
                me.orient();
            }
            bootLevel(stage);
            fadeStage = 2;
        } else if (fadeStage == 2 && step == -1) {
            fadeStage = 0;
            step = 0;
            noAnimate = false;
            if (stage != 5) {
                me.movementLocked = false;
            }
            me.cancelIdle();
            me.life = true;
            resetFade();
            if (stage != 4) {
                canLoopDown = false;
            }
            canLoopDownBoss = false;
            // gameMusic.setVolume(50);
            // cVol = 50;
            if (reviving) {
                // System.out.println("asd");
                reviving = false;
                // gameMusic.playLoop();
                me.hasDrowned = false;
            }
        } else if (fadeStage == 2 && step < 96) {
            Greenfoot.setSpeed(56);
            fadeIn();
        } else {
            Greenfoot.setSpeed(47);
        }
        if (canLoopDown && cVol >= 0) {
            cVol -= 2;
            gameMusic.setVolume(cVol);
        } else if (cVol < 36) {
            cVol += 2;
            gameMusic.setVolume(cVol);
        }
        if (canLoopDownBoss && cVolBoss >= 0) {
            cVolBoss -= 2;
            bossMusic.setVolume(cVolBoss);
        } else if (cVolBoss < 36) {
            cVolBoss += 2;
            bossMusic.setVolume(cVolBoss);
        }
    }

    // LEVELS

    public void lobby() throws java.io.IOException {
        GreenfootImage background = getBackground();
        background.setColor(new Color(212, 245, 255));
        background.fillRect(0, 0, getWidth(), getHeight());

        loadWorld(Data.lobby);

        me = new Player();
        addObject(me, 0, 1000);

        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }

        Orb.locked = false;

        setPaintOrder(Static.class, Text.class, BlackOverlay.class, GrassBlade.class, Player.class);

        if (gameIsMultiplayer) {

            you = new Partner();
            addObject(you, 0, 1000);
            String input = Greenfoot.ask("Game to join");
            String response = Network.joinGame(input);

            if (response.equals("-1")) {
                text.show("Can't connect to the server!", 300, 20, Color.ORANGE, new Color(0, 0, 0), 24);
                Greenfoot.stop();
                return;
            } else if (response.equals("0")) {
                text.show("This game is full!", 300, 20, Color.ORANGE, new Color(0, 0, 0), 24);
                Greenfoot.stop();
                return;
            } else if (response.equals("1")) {
                text.show(" Joined as player 1. ", 300, 20, Color.RED, new Color(0, 0, 0), 24);
                me.init("player");
                you.init("partner");
                me.setLocation(280, 200);
                playerNum = 1;
                me.startLocalPost();
                you.partnerGet();
            } else if (response.equals("2")) {
                text.show(" Joined as player 2. ", 300, 20, Color.BLUE, new Color(0, 0, 0), 24);
                me.init("partner");
                you.init("player");
                me.setLocation(320, 200);
                playerNum = 2;
                me.startLocalPost();
                you.partnerGet();
            } else {
                text.show("There was an error.", 300, 20, Color.ORANGE, new Color(0, 0, 0), 24);
            }

        } else {
            me.setLocation(300, 200);
            stat.show("darrow.png", 550, 290, "hover");
            arrowShown = true;
        }
    }

    public void earth1() {
        loadWorld(Data.earth1);
        keyReq = 3;
        me.waterProtected = false;
        recastPlayer(30, 320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
    }

    public void earth2() {
        loadWorld(Data.earth2);
        addObject(new SmallEnemy("sand-snake.png"), 230, 30);
        keyReq = 3;
        me.waterProtected = false;
        me.vSpeed = 0;
        recastPlayer(30, 320);
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
                group1.add((Tile) rock);
            } else if (rock.getX() > 330) {
                group2.add((Tile) rock);
            } else {
                group3.add((Tile) rock);
            }
        }
        Zone zone1 = new Zone(380, 280, 80, 40, group1);
        Zone zone2 = new Zone(380, 100, 140, 25, group2);
        Zone zone3 = new Zone(220, 100, 80, 30, group3);
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
    }

    public void earth3() {
        loadWorld(Data.earth3);
        addObject(new SmallEnemy("sand-snake.png"), 330, 260);
        addObject(new SmallEnemy("sand-snake.png"), 230, 140);
        keyReq = 3;
        me.waterProtected = false;
        me.vSpeed = 0;
        recastPlayer(30, 320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }

        ArrayList<Tile> group1 = new ArrayList<>();
        ArrayList<Tile> group2 = new ArrayList<>();
        ArrayList<Tile> group3 = new ArrayList<>();
        List<RocksHanging> allRocks = getObjects(RocksHanging.class);
        for (RocksHanging rock : allRocks) {
            if (rock.getX() > 360) {
                group1.add((Tile) rock);
            } else if (rock.getX() > 280) {
                group2.add((Tile) rock);
            } else {
                group3.add((Tile) rock);
            }
        }
        Zone zone1 = new Zone(400, 40, 240, 20, group1);
        Zone zone2 = new Zone(305, 300, 60, 20, group2);
        Zone zone3 = new Zone(180, 200, 60, 20, group3);
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
    }

    public void earthBoss() {
        loadWorld(Data.earthX);
        addObject(new Boss("Wicked-worm-idle-1.png"), 330, 260);
        keyReq = 1;
        me.waterProtected = false;
        me.vSpeed = 0;
        recastPlayer(30, 320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
        canLoopDown = true;
        canLoopDownBoss = false;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        bossMusic.playLoop();
                    }
                },
                900);
    }

    public void endBoss() {
        noAnimate = true;
        me.invulnerabilityClock = 500;
        me.movementLocked = true;
        me.vSpeed = 0;
        canLoopDownBoss = true;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Audio.playSound("win.wav");
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        me.movementLocked = false;
                                        keyObtained();
                                    }
                                },
                                2400);
                    }
                },
                1200);
    }

    public void end1() {
        bossMusic.stop();
        loadWorld(Data.lobby);
        keyReq = 3;
        me.waterProtected = true;
        me.switchCostume("player-idle.png");
        recastPlayer(21, 320);
        List<Orb> orbs = getObjects(Orb.class);
        for (Orb orb : orbs) {
            this.orb = orb;
        }
    }

    // -------------------------------------------------- //

    public void bootLevel(int lvl) {
        Key.v = -1;
        switch (lvl) {
            case 1:
                earth1();
                break;
            case 2:
                earth2();
                break;
            case 3:
                earth3();
                break;
            case 4:
                earthBoss();
                break;
            case 5:
                end1();
                break;
        }
    }

    public void restartLevel() {
        // gameMusic.stop();
        canLoopDown = true;
        canLoopDownBoss = true;
        Orb.locked = true;
        keyReq = 3;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Audio.playSound("lose.wav");
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        reviving = true;
                                        fadeStage = 1;
                                        me.movementLocked = true;
                                    }
                                },
                                700);
                    }
                },
                1000);
    }

    public void recastPlayer(int x, int y) {
        me.waterDeath = false;
        me.lockedMover = false;
        if (gameIsMultiplayer) {
            int z = (playerNum == 1) ? -1 : 1;
            me.setLocation(x + (16 * z), y);
        } else {
            me.setLocation(x, y);
        }
    }

    public void loadWorld(int[][] num) {
        for (int i = 0; i < num.length; i++) {
            for (int g = 0; g < num[i].length; g++) {
                int x = g * 20 + 10;
                int y = i * 20 + 10;
                Tile tile = TileMap.getTile(num[i][g]);
                if (tile != null) {
                    addObject(tile, x, y);
                }
            }
        }
    }

    public void stopped() {
        try {
            me.stopLocalPost();
            you.stopGet();
        } catch (Exception e) {
        }
    }

    public void unload() {
        List<Tile> u1 = getObjects(Tile.class);
        List<Text> u2 = getObjects(Text.class);
        List<Static> u3 = getObjects(Static.class);
        List<SmallEnemy> u4 = getObjects(SmallEnemy.class);
        List<Boss> u5 = getObjects(Boss.class);
        List<Actor> objects = new ArrayList<>();
        objects.addAll(u1);
        objects.addAll(u2);
        objects.addAll(u3);
        objects.addAll(u4);
        objects.addAll(u5);
        removeObjects(objects);
    }

    public void fadeOut() {
        fadeStorage[step] = new BlackOverlay();
        addObject(fadeStorage[step], current.x * 50 + 25, current.y * 50 + 25);
        if (reachedBoundary()) {
            direction = direction.next();
            updateBounds();
        }
        current.move(direction);
        step++;
    }

    public void fadeIn() {
        if (step >= 0) {
            removeObject(fadeStorage[95 - step]);
            fadeStorage[95 - step] = null;
            step--;
        }
    }

    private void resetFade() {
        fadeStorage = new BlackOverlay[96];
        step = 0;
        direction = Direction.RIGHT;
        current = new Point(0, 0);
        bounds = new int[][] { { 12, 9 }, { -2, -1 } };
    }

    private boolean reachedBoundary() {
        switch (direction) {
            case RIGHT:
                return current.x + 1 == bounds[0][0];
            case DOWN:
                return current.y + 1 == bounds[0][1];
            case LEFT:
                return current.x - 1 == bounds[1][0];
            case UP:
                return current.y - 1 == bounds[1][1];
        }
        return false;
    }

    private void updateBounds() {
        switch (direction) {
            case RIGHT:
                bounds[0][0]--;
                break;
            case DOWN:
                bounds[0][1]--;
                break;
            case LEFT:
                bounds[1][0]++;
                break;
            case UP:
                bounds[1][1]++;
                break;
        }
    }
}
