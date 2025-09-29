package ak4ak.utillib.TextInputManager;

import ak4ak.utillib.Main.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CommandInput {
    public static JavaPlugin plugin;
    private static HashMap<Player,String>commandMap;
    private static HashMap<Player, BiConsumer<Player,String>>consumerMap;

    public static void run(){
        if (commandMap == null){
            commandMap = new HashMap<>();
        }
        if (consumerMap == null){
            consumerMap = new HashMap<>();
        }
    }

    public static void createCommand(Player player,String comm){

        if (commandMap == null){
            commandMap = new HashMap<>();
        }
        commandMap.put(player,comm);
        player.closeInventory();

    }

    public static void createCommand(Player player,String comm, String playerComment){

        if (commandMap == null){
            commandMap = new HashMap<>();
        }
        commandMap.put(player,comm);
        Util.sendmessage(player,playerComment);
        player.closeInventory();

    }

    public static void createCommand(Player player,String comm, BiConsumer<Player,String>consumer){

        if (commandMap == null){
            commandMap = new HashMap<>();
        }
        if (consumerMap == null){
            consumerMap = new HashMap<>();
        }
        commandMap.put(player,comm);
        consumerMap.put(player,consumer);
        player.closeInventory();

    }

    public static void createCommand(Player player, BiConsumer<Player, String> consumer) {
        if (consumerMap == null) {
            consumerMap = new HashMap<>();
        }

        // Заменяем старый consumer новым
        consumerMap.put(player, consumer);


    }


//    public static void registerPlayer(Player player, Consumer<String> action) {
//        commandMap.put(player, action);
//    }

    // Удаление игрока из сессии
    public static void unregisterPlayerCommand(Player player) {
        if (commandMap != null) {
            commandMap.remove(player);
        }
    }
    public static void unregisterPlayerConsumer(Player player){
        if (consumerMap != null) {
            consumerMap.remove(player);
        }
    }

    public static HashMap<Player, String> getCommandMap(){
        return commandMap;
    }
    // Проверка: находится ли игрок в режиме ожидания?
    public static boolean isWaiting(Player player) {
        return commandMap.containsKey(player);
    }
    public static void registerEvent(JavaPlugin plugin){
        CommandInput.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new TextInputListener(), plugin);

        if (consumerMap == null){
            consumerMap = new HashMap<>();
        }
        if (commandMap == null){
            commandMap = new HashMap<>();
        }
    }

    public static void executionFunc(Player player, String mess) {
        BiConsumer<Player, String> consumer = consumerMap.get(player);
        if (consumer != null) { // Если он есть
            consumer.accept(player, mess); // Выполняем его
            consumerMap.remove(player);
        }
    }



}
