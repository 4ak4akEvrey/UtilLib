package ak4ak.utillib.API.Command;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandAPI implements Listener{

    private static final CommandNode ROOT = new CommandNode("ROOT");

    // player -> command -> timeUntil
    private static final Map<String, Map<String, Long>> cooldowns = new HashMap<>();

    // =========================
    //  REGISTRATION
    // =========================


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        CommandAPI.onCommandEvent(event);
    }




    public static CommandNode register(String command, Consumer<PlayerCommandPreprocessEvent> handler) {
        String[] parts = command.toLowerCase().split(" ");

        CommandNode node = ROOT;
        for (String part : parts) {
            node = node.getOrCreate(part);
        }

        return node.setHandler(handler);
    }

    // Ловит ВСЕ команды начинающиеся с prefix
    public static CommandNode registerWildcard(String prefix, Consumer<PlayerCommandPreprocessEvent> handler) {
        return register(prefix.toLowerCase(), handler);
    }

    // =========================
    //  EVENT HANDLER
    // =========================

    public static void onCommandEvent(PlayerCommandPreprocessEvent event) {

        String playerName = event.getPlayer().getName();
        String[] args = event.getMessage().substring(1).toLowerCase().split(" ");

        CommandNode node = ROOT;
        CommandNode lastNodeWithHandler = null;

        StringBuilder builtCommand = new StringBuilder();

        // Поиск в дереве (no loops over all commands!)
        for (int i = 0; i < args.length; i++) {

            String part = args[i];

            if (i > 0) builtCommand.append(" ");
            builtCommand.append(part);

            node = node.get(part);

            if (node == null)
                break;

            // Запоминаем ближайшую команду с обработчиком
            if (node.getHandler() != null)
                lastNodeWithHandler = node;
        }

        // Нет обработчиков
        if (lastNodeWithHandler == null)
            return;

        // Кулдаун
        String key = builtCommand.toString();
        if (hasCooldown(playerName, key)) {
            event.getPlayer().sendMessage("§cКоманда на кулдауне!");
            event.setCancelled(true);
            return;
        }

        // Ставим кулдаун
        setCooldown(playerName, key, lastNodeWithHandler.getCooldownMillis());

        // Выполняем handler
        lastNodeWithHandler.getHandler().accept(event);
    }

    // =========================
    //   COOLDOWN LOGIC
    // =========================

    public static boolean hasCooldown(String player, String command) {
        long now = System.currentTimeMillis();

        Map<String, Long> map = cooldowns.get(player);
        if (map == null) return false;

        long until = map.getOrDefault(command, 0L);
        return until > now;
    }

    public static void setCooldown(String player, String command, long duration) {
        if (duration <= 0) return;

        cooldowns
                .computeIfAbsent(player, k -> new HashMap<>())
                .put(command, System.currentTimeMillis() + duration);
    }
}
