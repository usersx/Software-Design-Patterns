package com.gdx.game.event;

import com.gdx.game.entities.Entity;

/**
 * 状态变化事件
 * 处理实体状态变化的通知
 */
public class StateChangeEvent implements EntityEvent {
    private Entity entity;
    private Entity.State oldState;
    private Entity.State newState;

    public StateChangeEvent(Entity entity, Entity.State oldState, Entity.State newState) {
        this.entity = entity;
        this.oldState = oldState;
        this.newState = newState;
    }

    @Override
    public void execute(Entity entity) {
        // 通知所有相关系统状态变化
        entity.getEventManager().dispatchEvent("stateChange", entity, oldState, newState);
    }

    public Entity.State getOldState() {
        return oldState;
    }

    public Entity.State getNewState() {
        return newState;
    }
} 