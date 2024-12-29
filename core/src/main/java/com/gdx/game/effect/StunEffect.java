package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 眩晕效果
 * 使实体无法移动和攻击
 */
public class StunEffect extends BaseEffect {
    private boolean applied;
    
    /**
     * 创建眩晕效果
     * @param duration 持续时间(秒)
     */
    public StunEffect(float duration) {
        super(duration, 1.0f); // 眩晕效果强度固定为1
        this.applied = false;
    }
    
    @Override
    protected void applyEffect(Entity entity, float delta) {
        if (!applied) {
            // 设置为不可移动状态
            entity.setState(Entity.State.IMMOBILE);
            
            // 添加眩晕视觉效果
            entity.addVisualEffect("stun");
            
            applied = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        boolean finished = super.isFinished();
        if (finished && applied && target != null) {
            // 效果结束时恢复正常状态
            target.setState(Entity.State.IDLE);
            target.removeVisualEffect("stun");
        }
        return finished;
    }
} 