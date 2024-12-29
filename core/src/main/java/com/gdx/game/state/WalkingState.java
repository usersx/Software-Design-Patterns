package com.gdx.game.state;

import com.gdx.game.entities.Entity;

/**
 * 行走状态
 * 处理实体在移动时的行为
 */
public class WalkingState implements EntityState {
    @Override
    public void enter(Entity entity) {
        entity.setState(Entity.State.WALKING);
        entity.setMoving(true);
    }

    @Override
    public void update(Entity entity, float delta) {
        // 检查是否停止移动
        if (!entity.isMoving()) {
            entity.setState(Entity.State.IDLE);
        }
        
        // 检查是否进入战斗
        if (entity.isInCombat()) {
            entity.setState(Entity.State.IMMOBILE);
        }
    }

    @Override
    public void exit(Entity entity) {
        entity.setMoving(false);
    }
} 