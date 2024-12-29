package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 减速效果
 * 降低实体移动速度
 */
public class SlowEffect extends BaseEffect {
    private float slowPercentage; // 减速百分比(0-1)
    private boolean applied;      // 是否已应用减速
    
    /**
     * 创建减速效果
     * @param duration 持续时间(秒)
     * @param slowPercentage 减速百分比(0-1)
     */
    public SlowEffect(float duration, float slowPercentage) {
        super(duration, slowPercentage);
        this.slowPercentage = Math.min(1, Math.max(0, slowPercentage)); // 确保在0-1之间
        this.applied = false;
    }
    
    @Override
    protected void applyEffect(Entity entity, float delta) {
        if (!applied) {
            // 计算减速后的速度
            float currentSpeed = entity.getCurrentSpeed();
            float newSpeed = currentSpeed * (1 - slowPercentage);
            
            // 应用减速
            entity.setMoveSpeed(newSpeed);
            
            // 添加减速视觉效果
            entity.addVisualEffect("slow");
            
            applied = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        boolean finished = super.isFinished();
        if (finished && applied && target != null) {
            // 效果结束时恢复原速度
            target.setMoveSpeed(target.getBaseSpeed());
            target.removeVisualEffect("slow");
        }
        return finished;
    }
} 