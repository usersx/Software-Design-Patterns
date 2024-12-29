package com.gdx.game.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.gdx.game.entities.Entity;

/**
 * 优先级效果管理器
 * 管理效果的优先级和生命周期
 */
public class PriorityEffectManager {
    private PriorityQueue<PrioritizedEffect> effectQueue;
    private Map<Class<? extends EntityEffect>, EntityEffect> activeEffects;
    private Entity owner;

    public PriorityEffectManager(Entity owner) {
        this.owner = owner;
        this.effectQueue = new PriorityQueue<>(
            (e1, e2) -> Integer.compare(e1.getPriority().getValue(), e2.getPriority().getValue())
        );
        this.activeEffects = new HashMap<>();
    }

    /**
     * 添加新效果
     * 如果已存在同类效果,根据优先级决定是否替换
     */
    public void addEffect(EntityEffect effect, EffectPriority priority) {
        Class<? extends EntityEffect> effectClass = effect.getClass();
        EntityEffect existingEffect = activeEffects.get(effectClass);
        
        if (existingEffect instanceof PrioritizedEffect) {
            PrioritizedEffect existing = (PrioritizedEffect) existingEffect;
            // 只有新效果优先级更高时才替换
            if (priority.getValue() < existing.getPriority().getValue()) {
                removeEffect(effectClass);
            } else {
                return; // 保留现有效果
            }
        }

        PrioritizedEffect prioritizedEffect = new PrioritizedEffect(effect, priority);
        effectQueue.offer(prioritizedEffect);
        activeEffects.put(effectClass, effect);
        
        // 触发效果添加事件
        owner.getEventManager().dispatchEvent("effectApply", owner, effect);
    }

    /**
     * 移除指定类型的效果
     */
    public void removeEffect(Class<? extends EntityEffect> effectClass) {
        EntityEffect effect = activeEffects.remove(effectClass);
        if (effect != null) {
            effectQueue.removeIf(e -> e.getEffect().getClass() == effectClass);
            // 触发效果移除事件
            owner.getEventManager().dispatchEvent("effectRemove", owner, effect);
        }
    }

    /**
     * 更新所有活跃效果
     */
    public void update(float delta) {
        // 创建临时列表以避免并发修改
        List<PrioritizedEffect> effects = new ArrayList<>(effectQueue);
        
        // 按优先级顺序更新效果
        for (PrioritizedEffect effect : effects) {
            if (!effect.isFinished()) {
                effect.apply(owner, delta);
            } else {
                // 移除已结束的效果
                removeEffect(effect.getEffect().getClass());
            }
        }
    }

    /**
     * 检查是否存在指定类型的效果
     */
    public boolean hasEffect(Class<? extends EntityEffect> effectClass) {
        return activeEffects.containsKey(effectClass);
    }

    /**
     * 获取指定类型的效果
     */
    public EntityEffect getEffect(Class<? extends EntityEffect> effectClass) {
        return activeEffects.get(effectClass);
    }

    /**
     * 清除所有效果
     */
    public void clearEffects() {
        for (EntityEffect effect : activeEffects.values()) {
            owner.getEventManager().dispatchEvent("effectRemove", owner, effect);
        }
        activeEffects.clear();
        effectQueue.clear();
    }

    /**
     * 获取所有活跃效果
     */
    public List<EntityEffect> getActiveEffects() {
        return new ArrayList<>(activeEffects.values());
    }
} 