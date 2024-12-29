package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 增益效果
 * 提升实体属性
 */
public class BuffEffect extends BaseEffect implements DamageModifierEffect {
    private float damageBonus;    // 伤害加成(百分比)
    private float speedBonus;     // 速度加成(百分比)
    private boolean applied;      // 是否已应用加成
    
    /**
     * 创建增益效果
     * @param duration 持续时间(秒)
     * @param damageBonus 伤害加成百分比(0+)
     * @param speedBonus 速度加成百分比(0+)
     */
    public BuffEffect(float duration, float damageBonus, float speedBonus) {
        super(duration, 1.0f);
        this.damageBonus = Math.max(0, damageBonus);
        this.speedBonus = Math.max(0, speedBonus);
        this.applied = false;
    }
    
    @Override
    protected void applyEffect(Entity entity, float delta) {
        if (!applied) {
            // 应用速度加成
            float newSpeed = entity.getBaseSpeed() * (1 + speedBonus);
            entity.setMoveSpeed(newSpeed);
            
            // 添加增益视觉效果
            entity.addVisualEffect("buff");
            
            applied = true;
        }
    }
    
    @Override
    public float getDamageMultiplier() {
        return 1.0f + damageBonus;
    }
    
    @Override
    public boolean isFinished() {
        boolean finished = super.isFinished();
        if (finished && applied && target != null) {
            // 效果结束时恢复原速度
            target.setMoveSpeed(target.getBaseSpeed());
            target.removeVisualEffect("buff");
        }
        return finished;
    }
} 