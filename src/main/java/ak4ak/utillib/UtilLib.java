package ak4ak.utillib;



import ak4ak.utillib.API.Logger.DiscordBotManager;
import ak4ak.utillib.API.Logger.LogManager;
import ak4ak.utillib.API.Logger.MainLogger;
import ak4ak.utillib.Main.EventRegister;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class UtilLib extends JavaPlugin {

    public static JavaPlugin instance;
    private static LogManager playerLogger;
    private static LogManager modLogger;
    private static MainLogger mainLogger;
    private DiscordBotManager discordBot;

    @Override
    public void onEnable() {
        instance = this;
        EventRegister.load(instance);

        saveDefaultConfig();
        mainLogger = MainLogger.init(this);
        getLogger().info("UtilLib включен!");

        playerLogger = new LogManager( "Player");
        modLogger = new LogManager( "ModCommand");

        discordBot = new DiscordBotManager(this);
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            discordBot.start();
        });

    }

    @Override
    public void onDisable(){
        LogManager.flushAll();
        LogManager.clearRegistry();
        MainLogger.destroyInstance();

        if (discordBot != null) {
            discordBot.stop();
        }
    }

    public static LogManager getPlayerLogger() {
        return playerLogger;
    }

    public static LogManager getModLogger() {
        return modLogger;
    }

    public static void setInstance(JavaPlugin instance) {
        UtilLib.instance = instance;
    }
}