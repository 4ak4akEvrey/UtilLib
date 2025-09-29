package ak4ak.utillib.API.RandomTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

final class RandomSpawnTask {

    private final RandomSpawnBuilder cfg;
    private final Player player;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    RandomSpawnTask(RandomSpawnBuilder cfg, Player player) {
        this.cfg = cfg;
        this.player = player;
    }

    public void start() {
        Bukkit.getScheduler().runTaskAsynchronously(cfg.plugin, this::asyncSearch);
    }

    private void asyncSearch() {

        for (int i = 0; i < cfg.getMaxAttempts(); i++) {

            Location center = cfg.getCenter();
            int radius = cfg.getRadius();

            int x = center.getBlockX() + random.nextInt(-radius, radius);
            int z = center.getBlockZ() + random.nextInt(-radius, radius);

            int chunkX = x >> 4;
            int chunkZ = z >> 4;

            cfg.getWorld().getChunkAtAsync(chunkX, chunkZ, chunk -> {
                Location loc = validateSync(chunk, x, z);
                if (loc != null) {
                    teleport(loc);
                }
            });

            return; // chunk callback async-safe
        }

        fail();
    }
    private Location validateSync(Chunk chunk, int x, int z) {

        int y = chunk.getWorld().getHighestBlockYAt(x, z);
        Location loc = new Location(chunk.getWorld(), x + 0.5, y, z + 0.5);

        if (cfg.getAllowedBiomes() != null) {
            Biome biome = chunk.getWorld().getBiome(loc);
            if (!cfg.getAllowedBiomes().contains(biome)) return null;
        }

        if (cfg.isSafe()) {
            Block feet = loc.getBlock();
            Block head = feet.getRelative(0, 1, 0);
            Block ground = feet.getRelative(0, -1, 0);

            if (!ground.getType().isSolid()) return null;
            if (!feet.isPassable() || !head.isPassable()) return null;
        }

        return loc;
    }

    private void teleport(Location loc) {
        Bukkit.getScheduler().runTask(cfg.plugin, () -> {
            if (!player.isOnline()) return;
            player.teleportAsync(loc);
        });
    }

    private void fail() {
        Bukkit.getScheduler().runTask(cfg.plugin, () -> {
            if (cfg.getFailAction() != null) {
                cfg.getFailAction().accept(player);
            }
        });
    }
}



