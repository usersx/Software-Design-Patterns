package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 毒性效果
 * 造成持续伤害
 */
public class PoisonEffect extends BaseEffect {
    private float tickInterval;    // 伤害间隔
    private float tickTimer;       // 计时器
    private float damagePerTick;   // 每次伤害量
    
    public PoisonEffect(float duration, float damagePerSecond, float tickInterval) {
        super(duration, damagePerSecond, EffectType.POISON);
        this.tickInterval = tickInterval;
        this.tickTimer = 0;
        this.damagePerTick = damagePerSecond * tickInterval;
    }
    
    @Override
    protected void applyEffect(float delta) {
        tickTimer += delta;
        
        // 达到伤害间隔时造成伤害
        if (tickTimer >= tickInterval) {
            dealDamage();
            tickTimer -= tickInterval;
        }
    }
    
    @Override
    protected void onEffectStart() {
        // 添加毒性视觉效果
        target.addVisualEffect("poison");
        
        // 播放毒性音效
        target.playEffectSound("poison");
    }
    
    @Override
    protected void onEffectEnd() {
        // 移除毒性视觉效果
        target.removeVisualEffect("poison");
    }
    
    private void dealDamage() {
        if (target != null) {
            // 计算实际伤害
            float actualDamage = calculateDamage();
            
            // 应用伤害
            target.takeDamage(actualDamage);
            
            // 显示伤害数字
            target.showDamageNumber(actualDamage, true);
            
            // 播放伤害音效
            target.playHitSound();
        }
    }
    
    private float calculateDamage() {
        // 基础伤害
        float damage = damagePerTick;
        
        // 考虑目标的毒性抗性
        float poisonResistance = target.getPoisonResistance();
        damage *= (1 - poisonResistance);
        
        // 考虑其他修正因素
        damage *= target.getDamageModifier();
        
        return Math.max(1, damage);  // 确保至少造成1点伤害
    }
} 