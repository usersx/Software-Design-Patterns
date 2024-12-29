package com.gdx.game.state;

import com.gdx.game.entities.Entity;

/**
 * 静止状态
 * 处理实体在不能移动时的行为
 */
public class ImmobileState implements EntityState {
    @Override
    public void enter(Entity entity) {
        entity.setState(Entity.State.IMMOBILE);
        entity.setMoving(false);
    }

    @Override
    public void update(Entity entity, float delta) {
        // 在静止状态下,实体不能移动
        // 可以在这里添加其他静止状态下的行为
    }

    @Override
    public void exit(Entity entity) {
        // 退出静止状态时,转为空闲状态
        entity.setState(Entity.State.IDLE);
    }
} 