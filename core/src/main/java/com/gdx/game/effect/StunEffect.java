package com.gdx.game.effect;

import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityState;

/**
 * 眩晕效果
 * 使目标无法移动和攻击
 */
public class StunEffect extends BaseEffect {
    private EntityState previousState;  // 保存目标之前的状态
    
    public StunEffect(float duration) {
        super(duration, 1.0f, EffectType.STUN);  // 强度值在眩晕效果中不重要
    }
    
    @Override
    protected void applyEffect(float delta) {
        // 眩晕效果主要在开始和结束时起作用
        // 这里可以添加额外的视觉效果更新
        if (target != null) {
            // 更新眩晕特效的旋转角度等
            target.updateVisualEffect("stun", delta);
        }
    }
    
    @Override
    protected void onEffectStart() {
        if (target != null) {
            // 保存当前状态
            previousState = target.getCurrentState();
            
            // 设置为眩晕状态
            target.setState(EntityState.STUNNED);
            
            // 添加眩晕视觉效果
            target.addVisualEffect("stun");
            
            // 播放眩晕音效
            target.playEffectSound("stun");
            
            // 停止当前动作
            target.stopCurrentAction();
            
            // 显示眩晕图标
            target.showStatusIcon("stun");
        }
    }
    
    @Override
    protected void onEffectEnd() {
        if (target != null) {
            // 恢复之前的状态(如果目标没有其他眩晕效果)
            if (!target.hasEffect(StunEffect.class)) {
                target.setState(previousState);
            }
            
            // 移除眩晕视觉效果
            target.removeVisualEffect("stun");
            
            // 移除眩晕图标
            target.hideStatusIcon("stun");
            
            // 重置保存的状态
            previousState = null;
        }
    }
    
    @Override
    public String getDescription() {
        return String.format("眩晕效果 (%.1f秒)", getRemainingTime());
    }
    
    /**
     * 检查目标是否可以被眩晕
     * @param target 目标实体
     * @return true 如果目标可以被眩晕
     */
    public static boolean canBeStunned(Entity target) {
        // 检查目标是否免疫眩晕
        if (target.isImmuneToStun()) {
            return false;
        }
        
        // 检查目标当前状态
        EntityState state = target.getCurrentState();
        return state != EntityState.DEAD && 
               state != EntityState.INVULNERABLE;
    }
} 