package ak4ak.utillib.TextInputManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TextInputListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Сначала проверяем и исполняем consumer, если он есть
        CommandInput.executionFunc(player, message); // Выполнить consumer, если есть

        // Проверяем, находится ли игрок в режиме ожидания команды
        if (CommandInput.isWaiting(player)) {
            event.setCancelled(true); // Блокируем стандартный вывод чата

            // Получаем текст команды
            String textComm = CommandInput.getCommandMap().get(player);

            // Выполняем команду от имени игрока
            Bukkit.getScheduler().runTask(CommandInput.plugin, () -> {
                Bukkit.dispatchCommand(player, textComm + " " + message);
            });

            // Удаляем игрока из списка ожидания команды
            CommandInput.unregisterPlayerCommand(player);
        }
    }

}

