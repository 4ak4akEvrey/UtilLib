package ak4ak.utillib;


import ak4ak.utillib.MenuBuilder.MenuBuilder;
import ak4ak.utillib.MenuBuilder.MenuListener;
import ak4ak.utillib.TextInputManager.CommandInput;
import ak4ak.utillib.TextInputManager.TextInputListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Consumer;

public final class UtilLib extends JavaPlugin implements Listener {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        EventRegister.load(instance);

        getLogger().info("UtilLib включен!");


    }

    @Override
    public void onDisable(){



    }


}