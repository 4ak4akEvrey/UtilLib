package ak4ak.utillib.API.RandomTeleport;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public final class SpawnFinder {

    public static Location find(RandomSpawnBuilder c) {
        World world = c.getWorld();
        Location center = c.getCenter();

        for (int i = 0; i < c.getMaxAttempts(); i++) {

            int x = random(center.getBlockX(), c.getRadius());
            int z = random(center.getBlockZ(), c.getRadius());

            int y = world.getHighestBlockYAt(x, z);



            Location loc = new Location(world, x + 0.5, y + 1, z + 0.5);

            if (!checkBiome(c, loc)) continue;
//            if (c.isAvoidWater() && isWater(world, x, y, z)) continue;
            if (c.isSafe() && !isSafe(world, x, y, z)) continue;

            return loc;
        }
        return null;
    }

    /* ===== checks ===== */

    private static boolean checkBiome(RandomSpawnBuilder c, Location loc) {
        if (c.getAllowedBiomes() == null) return true;
        return c.getAllowedBiomes().contains(loc.getBlock().getBiome());
    }

    private static boolean isWater(World w, int x, int y, int z) {
        return w.getBlockAt(x, y, z).isLiquid();
    }

    private static boolean isSafe(World w, int x, int y, int z) {
        return w.getBlockAt(x, y + 1, z).isPassable()
                && w.getBlockAt(x, y + 2, z).isPassable()
                && w.getBlockAt(x, y, z).getType().isSolid();
    }

    private static int random(int center, int radius) {
        return center + ThreadLocalRandom.current().nextInt(-radius, radius);
    }
}
