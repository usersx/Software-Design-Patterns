package com.gdx.game.event;

import com.gdx.game.entities.Entity;
import java.util.*;

/**
 * 实体事件管理器
 * 负责事件的注册、分发和处理
 */
public class EntityEventManager {
    private Map<String, List<EntityEventListener>> listeners = new HashMap<>();

    public void addEventListener(String eventType, EntityEventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void removeEventListener(String eventType, EntityEventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public void dispatchEvent(String eventType, Entity entity, Object... args) {
        if (listeners.containsKey(eventType)) {
            EntityEvent event = createEvent(eventType, entity, args);
            for (EntityEventListener listener : listeners.get(eventType)) {
                listener.onEvent(event);
            }
        }
    }

    private EntityEvent createEvent(String eventType, Entity entity, Object... args) {
        switch (eventType) {
            case "stateChange":
                return new StateChangeEvent(entity, (Entity.State)args[0], (Entity.State)args[1]);
            case "effectApply":
                return new EffectApplyEvent(entity, args[0]);
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }
} 