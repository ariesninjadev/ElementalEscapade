import java.util.HashMap;
import java.util.function.Supplier;

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
        tiles.put(1,GroundTile::new);
        tiles.put(2,SandTile::new);
        tiles.put(3,GrassyGround::new);
    }
    /**
     * Constructor for objects of class TileMap
     */
    public TileMap()
    {
        
    }
    
    public static Tile getTile(Integer num)
    {
        if(num==0) return null;
        Supplier<Tile> supplier=tiles.get(num);
        return supplier.get();
    }
    
    
}
