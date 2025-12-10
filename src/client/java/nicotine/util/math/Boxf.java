package nicotine.util.math;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Boxf {
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public Boxf(AABB box) {
        this.minX = (float) box.minX;
        this.minY = (float) box.minY;
        this.minZ = (float) box.minZ;
        this.maxX = (float) box.maxX;
        this.maxY = (float) box.maxY;
        this.maxZ = (float) box.maxZ;
    }

    public Boxf move(Vec3 offset) {
        return new Boxf(new AABB(this.minX + offset.x, this.minY + offset.y, this.minZ + offset.z, this.maxX + offset.x, this.maxY + offset.y, this.maxZ + offset.z));
    }
}
