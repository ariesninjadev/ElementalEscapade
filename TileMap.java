import java.util.HashMap;
import java.util.function.Supplier;
import greenfoot.*;

/**
 * Write a description of class TileMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileMap  
{
    // instance variables - replace the example below with your own
    static HashMap<Integer,Supplier<Tile>> tiles = new HashMap<>();
    static {
        tiles.put(1,Dirt::new);
        tiles.put(2,GrassTLC::new);
        tiles.put(3,GrassTop::new);
        tiles.put(4,GrassTLS::new);
        tiles.put(5,GrassTLR::new);
        tiles.put(6,GrassTopRotated::new);
        tiles.put(7,GrassBlade::new);
        tiles.put(8,Orb::new);
    }
    /**
     * Constructor for objects of class TileMap
     */
    public TileMap()
    {
        
    }
    
    public static Tile getTile(Integer num)
    {
        if (num == 0) {
            return null;
        }
        boolean isHoriz = false;
        boolean isVert = false;
        if (num < 0) {
            num *= -1;
            isHoriz = true;
        }
        if (num > 1000) {
            num -= 1000;
            isVert = true;
        }
        Supplier<Tile> supplier=tiles.get(num);
        Tile tile = supplier.get();
        if (isHoriz) {
            GreenfootImage img = tile.getImage();
            img.mirrorHorizontally();
            tile.setImage(img);
        }
        if (isVert) {
            GreenfootImage img = tile.getImage();
            img.mirrorVertically();
            tile.setImage(img);
        }
        return tile;
    }
    
    
}
