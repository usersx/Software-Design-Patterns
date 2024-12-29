package com.gdx.game.event;

import com.gdx.game.entities.Entity;

/**
 * 实体事件接口
 * 定义事件的基本行为
 */
public interface EntityEvent {
    void execute(Entity entity);
} 