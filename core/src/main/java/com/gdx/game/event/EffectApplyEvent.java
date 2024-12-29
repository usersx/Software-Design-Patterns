package com.gdx.game.event;

import com.gdx.game.entities.Entity;
import com.gdx.game.effect.EntityEffect;

/**
 * 效果应用事件
 * 处理实体效果应用的通知
 */
public class EffectApplyEvent implements EntityEvent {
    private Entity entity;
    private Object effect;

    public EffectApplyEvent(Entity entity, Object effect) {
        this.entity = entity;
        this.effect = effect;
    }

    @Override
    public void execute(Entity entity) {
        // 通知所有相关系统效果应用
        entity.getEventManager().dispatchEvent("effectApply", entity, effect);
    }

    public Object getEffect() {
        return effect;
    }
} 