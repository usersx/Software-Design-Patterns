package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 减速效果
 * 降低目标的移动速度
 */
public class SlowEffect extends BaseEffect {
    private float slowPercentage;  // 减速百分比(0-1)
    
    public SlowEffect(float duration, float slowPercentage) {
        super(duration, slowPercentage, EffectType.SLOW);
        this.slowPercentage = Math.min(1.0f, Math.max(0, slowPercentage));  // 限制在0-1之间
    }
    
    @Override
    protected void applyEffect(float delta) {
        // 减速效果主要在开始和结束时起作用
        // 这里可以添加额外的视觉效果更新
        if (target != null) {
            // 更新减速特效
            target.updateVisualEffect("slow", delta);
        }
    }
    
    @Override
    protected void onEffectStart() {
        if (target != null) {
            // 应用减速效果
            target.modifyMoveSpeed(-slowPercentage);
            
            // 添加减速视觉效果
            target.addVisualEffect("slow");
            
            // 播放减速音效
            target.playEffectSound("slow");
            
            // 显示减速图标
            target.showStatusIcon("slow");
        }
    }
    
    @Override
    protected void onEffectEnd() {
        if (target != null) {
            // 恢复移动速度
            target.modifyMoveSpeed(slowPercentage);
            
            // 移除减速视觉效果
            target.removeVisualEffect("slow");
            
            // 移除减速图标
            target.hideStatusIcon("slow");
        }
    }
    
    @Override
    public String getDescription() {
        return String.format("减速效果 (%.0f%% 降低, %.1f秒)", slowPercentage * 100, getRemainingTime());
    }
    
    public float getSlowPercentage() {
        return slowPercentage;
    }
    
    /**
     * 检查目标是否可以被减速
     * @param target 目标实体
     * @return true 如果目标可以被减速
     */
    public static boolean canBeSlowed(Entity target) {
        // 检查目标是否免疫减速
        if (target.isImmuneToSlow()) {
            return false;
        }
        
        // 检查目标当前减速状态
        float currentSlowAmount = target.getCurrentSlowAmount();
        return currentSlowAmount < 0.9f;  // 最多减速90%
    }
} 