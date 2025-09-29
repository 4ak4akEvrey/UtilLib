package ak4ak.utillib.API.ChatInput;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatInputListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        ChatInputInstance.handleChat(e);
    }
}
