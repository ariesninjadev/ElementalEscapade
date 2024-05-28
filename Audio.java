import greenfoot.*;

public class Audio  
{
    
    public GreenfootSound sound;
    
    public static boolean userHasSoundCard = true;
    
    public Audio(String a)
    {
        if (!userHasSoundCard) {return;}
        try {
            sound = new GreenfootSound(a);
        } catch (Exception e) {
            userHasSoundCard = false;
        }
    }
    
    public void playLoop() {
        if (!userHasSoundCard) {return;}
        try {
            sound.playLoop();
        } catch (Exception e) {
            userHasSoundCard = false;
        }
    }
    
    public void setVolume(int v) {
        if (!userHasSoundCard) {return;}
        try {
            sound.setVolume(v);
        } catch (Exception e) {
            userHasSoundCard = false;
        }
    }
    
    public void stop() {
        if (!userHasSoundCard) {return;}
        try {
            sound.stop();
        } catch (Exception e) {
            userHasSoundCard = false;
        }
    }
    
    public static void playSound(String a) {
        if (!userHasSoundCard) {return;}
        try {
            Greenfoot.playSound(a);
        } catch (Exception e) {
            userHasSoundCard = false;
        }
    }
}
