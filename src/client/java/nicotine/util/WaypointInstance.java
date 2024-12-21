package nicotine.util;

public class WaypointInstance {
    public String name;
    public String dimension;
    public String server;
    public int x;
    public int y;
    public int z;

    public WaypointInstance(String name, String dimension, String server, int x, int y, int z) {
        this.name = name;
        this.dimension = dimension;
        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
