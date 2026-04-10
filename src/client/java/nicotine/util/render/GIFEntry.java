package nicotine.util.render;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.Identifier;

public class GIFEntry {
    public Identifier identifier;
    public NativeImage nativeImage;
    public byte[] bytes;

    public GIFEntry(Identifier identifier, NativeImage nativeImage, byte[] bytes) {
        this.identifier = identifier;
        this.nativeImage = nativeImage;
        this.bytes = bytes;
    }
}
