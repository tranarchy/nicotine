package nicotine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static final Map<Class<?>, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public static <T> boolean post(T event) {
        List<EventListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) listener;
                if (!typedListener.onEvent(event)) {
                    return false;
                }
            }
        }
        return true;
    }

    @FunctionalInterface
    public interface EventListener<T> {
        boolean onEvent(T event);
    }
}

