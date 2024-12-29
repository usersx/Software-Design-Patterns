package com.gdx.game.state;

import com.gdx.game.entities.Entity;

/**
 * 战斗状态
 * 处理实体在战斗时的行为
 */
public class CombatState implements EntityState {
    private float attackCooldown = 0;
    private static final float ATTACK_COOLDOWN_TIME = 1.0f;

    @Override
    public void enter(Entity entity) {
        entity.setState(Entity.State.IMMOBILE);
        entity.setInCombat(true);
    }

    @Override
    public void update(Entity entity, float delta) {
        // 更新攻击冷却时间
        if (attackCooldown > 0) {
            attackCooldown -= delta;
        }

        // 检查是否可以攻击
        if (entity.canAttack() && attackCooldown <= 0) {
            entity.attack();
            attackCooldown = ATTACK_COOLDOWN_TIME;
        }

        // 检查是否需要切换到其他状态
        if (!entity.isInCombat()) {
            if (entity.isMoving()) {
                entity.setState(Entity.State.WALKING);
            } else {
                entity.setState(Entity.State.IDLE);
            }
        }
    }

    @Override
    public void exit(Entity entity) {
        entity.setInCombat(false);
        entity.setState(Entity.State.IDLE);
        attackCooldown = 0;
    }
} 