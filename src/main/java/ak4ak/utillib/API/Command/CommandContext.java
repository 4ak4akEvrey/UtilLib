package ak4ak.utillib.API.Command;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandContext {

    private final Player player;
    private final PlayerCommandPreprocessEvent event;
    private final String[] args;
    private final CommandNode node;

    private boolean cancelled = false;

    public CommandContext(Player player, PlayerCommandPreprocessEvent event, String[] args, CommandNode node) {
        this.player = player;
        this.event = event;
        this.args = args;
        this.node = node;
    }

    public Player player() { return player; }
    public String[] args() { return args; }
    public CommandNode node() { return node; }

    public Player getPlayer() {
        return player;
    }

    public PlayerCommandPreprocessEvent getEvent() {
        return event;
    }

    public String[] getArgs() {
        return args;
    }

    public CommandNode getNode() {
        return node;
    }

    public void cancel() {
        this.cancelled = true;
        event.setCancelled(true);
    }

    public boolean isCancelled() { return cancelled; }
}

