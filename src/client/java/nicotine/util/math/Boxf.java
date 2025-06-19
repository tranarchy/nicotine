package nicotine.util.math;

import net.minecraft.util.math.Box;

public class Boxf {
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public Boxf(Box box) {
        this.minX = (float)box.minX;
        this.minY = (float)box.minY;
        this.minZ = (float)box.minZ;
        this.maxX = (float)box.maxX;
        this.maxY = (float)box.maxY;
        this.maxZ = (float)box.maxZ;
    }
}
