import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Orb extends Tile
{
    int clock = 0;
    int costume = 0;
    
    public static boolean locked = false;
    
    public void act()
    {
        clock++;
        if (clock==5) {
            clock = 0;
            nextCostume();
        }
    }
    
    private void nextCostume() {
        String[] costumes = {
            "portal-0.png",
            "portal-1.png",
            "portal-2.png",
            "portal-3.png",
            "portal-4.png",
            "portal-5.png"
        };
        costume = (costume + 1) % costumes.length;
        setImage(costumes[costume]);
        if (locked) {
            GreenfootImage img = getImage();
            for(int i=0; i<img.getWidth(); i++)
            {
                for(int j=0; j<img.getHeight(); j++)
                {
                    Color col=img.getColorAt(i,j);
                    if (col.getAlpha() == 0) {
                        img.setColorAt(i,j,new Color(0,0,0,0));
                    } else {
                        int r=col.getRed();
                        int g=col.getGreen();
                        int b=col.getBlue();
                        int avg=(r+g+b)/3;
                        img.setColorAt(i,j,new Color(avg, avg, avg));
                    }
                }
                setImage(img);
            }
        }
    }
    
    public boolean isCollidable() {
        return false;
    }
}
