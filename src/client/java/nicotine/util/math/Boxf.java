package nicotine.util.math;

import net.minecraft.util.math.Box;

public class Boxf {
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;

    public Boxf(Box box) {
        this.minX = (float)box.minX;
        this.minY = (float)box.minY;
        this.minZ = (float)box.minZ;
        this.maxX = (float)box.maxX;
        this.maxY = (float)box.maxY;
        this.maxZ = (float)box.maxZ;
    }
}
