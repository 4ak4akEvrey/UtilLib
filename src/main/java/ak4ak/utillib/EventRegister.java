package ak4ak.utillib;

import ak4ak.utillib.MenuBuilder.MenuBuilder;

import ak4ak.utillib.TextInputManager.CommandInput;
import org.bukkit.plugin.java.JavaPlugin;

public class EventRegister {
    public static void load(JavaPlugin plugin){
//        TextInputManager.registerEvent(plugin);
        CommandInput.registerEvent(plugin);
        MenuBuilder.registerEvent(plugin);
    }
}
