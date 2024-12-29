package com.gdx.game.effect;

import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;

public class EntityEffectManager {
    private Entity entity;
    private Array<EntityEffect> activeEffects;
    private Array<EntityEffect> effectsToRemove;
    
    public EntityEffectManager(Entity entity) {
        this.entity = entity;
        this.activeEffects = new Array<>();
        this.effectsToRemove = new Array<>();
    }
    
    public void addEffect(EntityEffect effect) {
        // 检查是否已存在相同类型的效果
        for (EntityEffect activeEffect : activeEffects) {
            if (activeEffect.getClass() == effect.getClass()) {
                // 如果新效果更强,则移除旧效果
                if (effect.getStrength() > activeEffect.getStrength()) {
                    removeEffect(activeEffect);
                } else {
                    return;
                }
            }
        }
        
        // 添加新效果
        activeEffects.add(effect);
        effect.onApply(entity);
        
        // 通知观察者
        entity.notifyEffectAdded(effect);
    }
    
    public void removeEffect(EntityEffect effect) {
        if (activeEffects.contains(effect, true)) {
            effectsToRemove.add(effect);
            effect.onRemove(entity);
            
            // 通知观察者
            entity.notifyEffectRemoved(effect);
        }
    }
    
    public void update(float delta) {
        // 更新所有效果
        for (EntityEffect effect : activeEffects) {
            effect.update(delta);
            
            // 检查效果是否结束
            if (effect.isFinished()) {
                effectsToRemove.add(effect);
            }
        }
        
        // 移除已结束的效果
        for (EntityEffect effect : effectsToRemove) {
            activeEffects.removeValue(effect, true);
            effect.onRemove(entity);
            
            // 通知观察者
            entity.notifyEffectRemoved(effect);
        }
        effectsToRemove.clear();
    }
    
    public Array<EntityEffect> getActiveEffects() {
        return activeEffects;
    }
    
    public boolean hasEffect(Class<? extends EntityEffect> effectType) {
        for (EntityEffect effect : activeEffects) {
            if (effectType.isInstance(effect)) {
                return true;
            }
        }
        return false;
    }
    
    public <T extends EntityEffect> T getEffect(Class<T> effectType) {
        for (EntityEffect effect : activeEffects) {
            if (effectType.isInstance(effect)) {
                return effectType.cast(effect);
            }
        }
        return null;
    }
    
    public void clear() {
        // 移除所有效果
        for (EntityEffect effect : activeEffects) {
            effect.onRemove(entity);
            
            // 通知观察者
            entity.notifyEffectRemoved(effect);
        }
        activeEffects.clear();
        effectsToRemove.clear();
    }
}