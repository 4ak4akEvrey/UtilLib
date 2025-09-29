package ak4ak.utillib.Main;

import ak4ak.utillib.API.ChatActionBuilder.ChatPromptExecutor;
import ak4ak.utillib.API.ChatInput.ChatInputListener;
import ak4ak.utillib.API.Command.CommandAPI;

import ak4ak.utillib.MenuBuilder.MenuManager;
import ak4ak.utillib.TextInputManager.CommandInput;
import ak4ak.utillib.UtilLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class EventRegister {
    public static void load(JavaPlugin plugin){
//        TextInputManager.registerEvent(plugin);
        CommandInput.registerEvent(plugin);

        Bukkit.getPluginManager().registerEvents(new ChatInputListener(), plugin);

        Bukkit.getPluginManager().registerEvents(new MenuManager(),plugin);

        Bukkit.getPluginManager().registerEvents(new CommandAPI(), plugin);


        plugin.getCommand("chataction").setExecutor(new ChatPromptExecutor());


        plugin.saveDefaultConfig();


    }
}
