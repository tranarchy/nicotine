package nicotine.util.waypoint;

import org.apache.commons.lang3.StringUtils;

public class Waypoint {
    public String name;
    public String dimension;
    public String server;
    public int x;
    public int y;
    public int z;

    public Waypoint(String name, String dimension, String server, int x, int y, int z) {
        this.name = name;
        this.dimension = dimension;
        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getFormattedDimensionName() {
        return StringUtils.capitalize(this.dimension.split(":")[1].replace("_", " "));
    }
}
