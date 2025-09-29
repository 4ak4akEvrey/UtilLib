package ak4ak.utillib.API.RandomTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnRequest {

    private final Player player;
    private final RandomSpawnBuilder config;

    public SpawnRequest(Player player, RandomSpawnBuilder config) {
        this.player = player;
        this.config = config;
    }

    public void execute() {
        Location loc = SpawnFinder.find(config);

        if (loc == null) {
            if (config.getFailAction() != null) {
                config.getFailAction().accept(player);
            }
            return;
        }

        player.teleport(loc);
    }
}
