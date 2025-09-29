package ak4ak.utillib.API.ChatActionBuilder;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PromptManager {

    // promptId → (buttonId → action)
    private static final Map<String, Map<String, Consumer<Player>>> prompts = new HashMap<>();

    public static void registerButton(String promptId, String buttonId, Consumer<Player> action) {
        prompts.putIfAbsent(promptId, new HashMap<>());
        prompts.get(promptId).put(buttonId, action);
    }

    public static void run(Player player, String promptId, String buttonId) {

        Map<String, Consumer<Player>> group = prompts.remove(promptId);
        if (group == null) return; // уже использовано

        Consumer<Player> act = group.get(buttonId);
        if (act != null) act.accept(player);

        // После выбора — все кнопки недействительны автоматически
    }
}






