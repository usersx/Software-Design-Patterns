package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 增益效果
 * 提升目标的攻击力和速度
 */
public class BuffEffect extends BaseEffect {
    private float damageBonus;    // 攻击力提升百分比(0-1)
    private float speedBonus;     // 速度提升百分比(0-1)
    
    public BuffEffect(float duration, float damageBonus, float speedBonus) {
        super(duration, Math.max(damageBonus, speedBonus), EffectType.BUFF);
        this.damageBonus = Math.min(1.0f, Math.max(0, damageBonus));    // 限制在0-1之间
        this.speedBonus = Math.min(1.0f, Math.max(0, speedBonus));      // 限制在0-1之间
    }
    
    @Override
    protected void applyEffect(float delta) {
        // 增益效果主要在开始和结束时起作用
        // 这里可以添加额外的视觉效果更新
        if (target != null) {
            // 更新增益特效
            target.updateVisualEffect("buff", delta);
        }
    }
    
    @Override
    protected void onEffectStart() {
        if (target != null) {
            // 应用攻击力提升
            target.modifyAttackPower(damageBonus);
            
            // 应用速度提升
            target.modifyMoveSpeed(speedBonus);
            
            // 添加增益视觉效果
            target.addVisualEffect("buff");
            
            // 播放增益音效
            target.playEffectSound("buff");
            
            // 显示增益图标
            target.showStatusIcon("buff");
        }
    }
    
    @Override
    protected void onEffectEnd() {
        if (target != null) {
            // 移除攻击力提升
            target.modifyAttackPower(-damageBonus);
            
            // 移除速度提升
            target.modifyMoveSpeed(-speedBonus);
            
            // 移除增益视觉效果
            target.removeVisualEffect("buff");
            
            // 移除增益图标
            target.hideStatusIcon("buff");
        }
    }
    
    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder("增益效果 (");
        if (damageBonus > 0) {
            desc.append(String.format("攻击+%.0f%% ", damageBonus * 100));
        }
        if (speedBonus > 0) {
            desc.append(String.format("速度+%.0f%% ", speedBonus * 100));
        }
        desc.append(String.format("%.1f秒)", getRemainingTime()));
        return desc.toString();
    }
    
    public float getDamageBonus() {
        return damageBonus;
    }
    
    public float getSpeedBonus() {
        return speedBonus;
    }
    
    /**
     * 检查目标是否可以被增益
     * @param target 目标实体
     * @return true 如果目标可以被增益
     */
    public static boolean canBeBuffed(Entity target) {
        // 检查目标是否免疫增益
        if (target.isImmuneToBuff()) {
            return false;
        }
        
        // 检查目标当前增益状态
        float currentBuffAmount = target.getCurrentBuffAmount();
        return currentBuffAmount < 2.0f;  // 最多提升200%
    }
} 