package com.gdx.game.state;

import com.gdx.game.entities.Entity;

/**
 * 空闲状态
 * 处理实体在空闲时的行为
 */
public class IdleState implements EntityState {
    @Override
    public void enter(Entity entity) {
        entity.setState(Entity.State.IDLE);
        entity.setMoving(false);
    }

    @Override
    public void update(Entity entity, float delta) {
        // 在空闲状态下检查是否开始移动
        if (entity.isMoving()) {
            entity.setState(Entity.State.WALKING);
        }
    }

    @Override
    public void exit(Entity entity) {
        // 退出空闲状态时不需要特殊处理
    }
} 