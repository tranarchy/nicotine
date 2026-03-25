package nicotine.events;

import org.joml.Vector2i;

public class MouseScrollEvent {
    public Vector2i cords;

    public MouseScrollEvent(Vector2i cords) {
        this.cords = cords;
    }
}
