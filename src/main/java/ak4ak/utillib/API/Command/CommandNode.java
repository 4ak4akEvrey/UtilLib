package ak4ak.utillib.API.Command;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandNode {

    private final String name;
    private Consumer<PlayerCommandPreprocessEvent> handler;
    private long cooldownMillis = 0;

    private final Map<String, CommandNode> children = new HashMap<>();

    public CommandNode(String name) {
        this.name = name;
    }

    public CommandNode setHandler(Consumer<PlayerCommandPreprocessEvent> handler) {
        this.handler = handler;
        return this;
    }

    public CommandNode cooldown(int seconds) {
        this.cooldownMillis = seconds * 1000L;
        return this;
    }

    public CommandNode getOrCreate(String arg) {
        return children.computeIfAbsent(arg, CommandNode::new);
    }

    public CommandNode get(String arg) {
        return children.get(arg);
    }

    public Consumer<PlayerCommandPreprocessEvent> getHandler() {
        return handler;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public Map<String, CommandNode> getChildren() {
        return children;
    }
}


