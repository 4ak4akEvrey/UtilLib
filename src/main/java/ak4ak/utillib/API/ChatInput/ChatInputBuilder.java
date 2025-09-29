package ak4ak.utillib.API.ChatInput;

import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatInputBuilder {

    private BiConsumer<Player, String> onSubmit;
    private Consumer<Player> onCancel;
    private int timeout = 20 * 60; // 1 минута

    public ChatInputBuilder onSubmit(BiConsumer<Player, String> onSubmit) {
        this.onSubmit = onSubmit;
        return this;
    }

    public ChatInputBuilder onCancel(Consumer<Player> onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public ChatInputBuilder timeout(int ticks) {
        this.timeout = ticks;
        return this;
    }

    public ChatInputTemplate build() {
        return new ChatInputTemplate(onSubmit, onCancel, timeout);
    }
}
