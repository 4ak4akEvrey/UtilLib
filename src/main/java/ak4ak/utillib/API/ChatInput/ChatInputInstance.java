package ak4ak.utillib.API.ChatInput;

import ak4ak.utillib.UtilLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatInputInstance {

    private static final Map<UUID, ChatInputInstance> active = new HashMap<>();

    private final Player player;
    private final BiConsumer<Player, String> onSubmit;
    private final Consumer<Player> onCancel;
    private final int timeout;

    private int taskId = -1;

    public ChatInputInstance(
            Player player,
            BiConsumer<Player, String> onSubmit,
            Consumer<Player> onCancel,
            int timeout
    ) {
        this.player = player;
        this.onSubmit = onSubmit;
        this.onCancel = onCancel;
        this.timeout = timeout;
    }

    public void start() {
        active.put(player.getUniqueId(), this);

        player.sendMessage("§7Введите текст в чат...");

        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                UtilLib.instance,
                () -> {
                    if (active.containsKey(player.getUniqueId())) {
                        cancel();
                    }
                },
                timeout
        );
    }

    public void finish(String message) {
        clear();
        onSubmit.accept(player, message);
    }

    public void cancel() {
        clear();
        if (onCancel != null) onCancel.accept(player);
    }

    private void clear() {
        active.remove(player.getUniqueId());
        if (taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
    }

    public static boolean handleChat(AsyncPlayerChatEvent e) {
        ChatInputInstance inst = active.get(e.getPlayer().getUniqueId());
        if (inst == null) return false;

        e.setCancelled(true);

        Bukkit.getScheduler().runTask(UtilLib.instance,
                () -> inst.finish(e.getMessage()));

        return true;
    }
}
