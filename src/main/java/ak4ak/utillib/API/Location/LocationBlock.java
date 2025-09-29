package ak4ak.utillib.API.Location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocationBlock {
    private final int x, y, z;
    private final String world;

    public LocationBlock(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public LocationBlock(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName(); // добавь это
    }

    public Location getLocation(){

        return new Location(Bukkit.getWorld(world), x,y,z);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationBlock)) return false;
        LocationBlock pos = (LocationBlock) o;
        return x == pos.x && y == pos.y && z == pos.z && world.equals(pos.world);
    }

    public static LocationBlock fromJson(Map<String, Object> jsonMap){
        if (jsonMap.isEmpty()) return null;

        int x = ((Number) (jsonMap.get("x"))).intValue();
        int y = ((Number) (jsonMap.get("y"))).intValue();
        int z = ((Number) (jsonMap.get("z"))).intValue();

        String world = (String) jsonMap.get("world");

        return new LocationBlock(x,y,z,world);
    }

    public Map<String,Object> toJson(){


        HashMap<String,Object> jsonMap = new HashMap<>();

        jsonMap.put("x", x);
        jsonMap.put("y", y);
        jsonMap.put("z",z);
        jsonMap.put("world",world);

        return jsonMap;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    @Override
    public String toString() {
        return "Position{" + "world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + '}';
    }

}
