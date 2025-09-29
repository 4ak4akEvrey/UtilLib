package ak4ak.utillib.API.ChatInput;

import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatInputTemplate {

    private final BiConsumer<Player, String> onSubmit;
    private final Consumer<Player> onCancel;
    private final int timeout;

    public ChatInputTemplate(
            BiConsumer<Player, String> onSubmit,
            Consumer<Player> onCancel,
            int timeout
    ) {
        this.onSubmit = onSubmit;
        this.onCancel = onCancel;
        this.timeout = timeout;
    }

    public void send(Player player) {
        new ChatInputInstance(player, onSubmit, onCancel, timeout).start();
    }
}

