package com.gdx.game.state;

import com.gdx.game.entities.Entity;

/**
 * 实体状态接口
 * 定义实体在不同状态下的行为
 */
public interface EntityState {
    /**
     * 进入状态时的行为
     */
    void enter(Entity entity);

    /**
     * 状态更新时的行为
     */
    void update(Entity entity, float delta);

    /**
     * 退出状态时的行为
     */
    void exit(Entity entity);
} 