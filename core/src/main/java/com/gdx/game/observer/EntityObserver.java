package com.gdx.game.observer;

import com.gdx.game.effect.EntityEffect;
import com.gdx.game.entities.Entity;

/**
 * 实体观察者接口
 * 定义了实体状态变化的回调方法
 */
public interface EntityObserver {
    /**
     * 当实体生命值变化时调用
     */
    void onHealthChanged(Entity entity, float oldHealth, float newHealth);

    /**
     * 当实体等级提升时调用
     */
    void onLevelUp(Entity entity, int oldLevel, int newLevel);

    /**
     * 当实体状态效果改变时调用
     */
    void onStatusEffectChanged(Entity entity, EntityEffect effect, boolean added);
} 