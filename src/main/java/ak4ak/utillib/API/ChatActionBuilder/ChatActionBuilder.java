package ak4ak.utillib.API.ChatActionBuilder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatActionBuilder {

    private final String message;
    private final List<PromptButton> buttons = new ArrayList<>();

    public ChatActionBuilder(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public ChatActionBuilder addButton(String text, Consumer<Player> action) {
        buttons.add(new PromptButton(text, action));
        return this;
    }

    public void sendTo(Player player) {

        // Создаем ID группы
        String promptId = UUID.randomUUID().toString();

        Component msg = Component.text(message + "\n");

        for (PromptButton b : buttons) {

            // id конкретной кнопки
            String buttonId = UUID.randomUUID().toString();

            // Регистрируем кнопку внутри группы
            PromptManager.registerButton(promptId, buttonId, b.action);

            Component button = Component.text(ChatColor.translateAlternateColorCodes('&', "&f[" + b.text + "&f]"))
                    .decorate(TextDecoration.BOLD)

                    .clickEvent(ClickEvent.runCommand("/chataction " + promptId + " " + buttonId));

            msg = msg.append(button).appendSpace();
        }

        player.sendMessage(msg);
    }

    private static class PromptButton {
        String text;
        Consumer<Player> action;

        public PromptButton(String text, Consumer<Player> action) {
            this.text = text;
            this.action = action;
        }
    }
}

