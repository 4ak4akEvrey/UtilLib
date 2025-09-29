package ak4ak.utillib.API.RandomTeleport;

import ak4ak.utillib.UtilLib;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.function.Consumer;

public final class RandomSpawnBuilder {

    public static JavaPlugin plugin = UtilLib.instance;

    private World world;
    private Location center;
    private int radius = 1000;
    private int maxAttempts = 150;
    private boolean async = true;
    private boolean safe = true;

    private Set<Biome> allowedBiomes;
    private Consumer<Player> failAction;

    public RandomSpawnBuilder() {

    }

    public static RandomSpawnBuilder create() {
        return new RandomSpawnBuilder();
    }

    public RandomSpawnBuilder world(World world) {
        this.world = world;
        return this;
    }

    public RandomSpawnBuilder center(Location center) {
        this.center = center.clone();
        return this;
    }

    public RandomSpawnBuilder radius(int radius) {
        this.radius = radius;
        return this;
    }

    public RandomSpawnBuilder maxAttempts(int attempts) {
        this.maxAttempts = attempts;
        return this;
    }

    public RandomSpawnBuilder sync() {
        this.async = false;
        return this;
    }

    public RandomSpawnBuilder safe() {
        this.safe = true;
        return this;
    }

    public RandomSpawnBuilder biomes(Biome... biomes) {
        this.allowedBiomes = Set.of(biomes);
        return this;
    }

    public RandomSpawnBuilder onFail(Consumer<Player> action) {
        this.failAction = action;
        return this;
    }

    public void spawn(Player player) {
        new RandomSpawnTask(this, player).start();
    }


    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public World getWorld() {
        return world;
    }

    public Location getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public boolean isAsync() {
        return async;
    }

    public boolean isSafe() {
        return safe;
    }

    public Set<Biome> getAllowedBiomes() {
        return allowedBiomes;
    }

    public Consumer<Player> getFailAction() {
        return failAction;
    }
}
