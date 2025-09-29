package ak4ak.utillib.Main;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerEvent {
    Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }
    public PlayerEvent teleport(int x, int y, int z){
        player.teleport(new Location(player.getWorld(),x,y,z));
        return this;
    }
    public PlayerEvent teleport(World world, int x, int y, int z){
        player.teleport(new Location(world,x,y,z));
        return this;
    }
    public PlayerEvent teleport(String world,int x, int y, int z){
        player.teleport(new Location(Bukkit.getWorld(world),x,y,z));
        return this;
    }
    public PlayerEvent setFoodLevel(int foodLevel){
        player.setFoodLevel(foodLevel);
        return this;
    }
    public PlayerEvent setDisplayName(String displayName){
        player.setDisplayName(displayName);
        return this;
    }
    public PlayerEvent setGameMode(GameMode gameMode){
        player.setGameMode(gameMode);
        return this;
    }
    public PlayerEvent sendMessage(String message){
        player.sendMessage(message);
        return this;
    }
    public PlayerEvent sendMessage(List<String>message){
        for (String mess : message)
            player.sendMessage(mess);
        return this;
    }
    public PlayerEvent sendMessage(String... message){
        for (String mess : message)
            player.sendMessage(mess);
        return this;
    }
    public PlayerEvent sendMessage(Component message){
            player.sendMessage(message);
        return this;
    }
    public PlayerEvent playSound(Sound sound){
       player.playSound(player.getLocation(),sound,1,1);
        return this;
    }
    public PlayerEvent playSound(Sound sound, float volume,float pitch){
        player.playSound(player.getLocation(),sound,volume,pitch);
        return this;
    }
    

}
