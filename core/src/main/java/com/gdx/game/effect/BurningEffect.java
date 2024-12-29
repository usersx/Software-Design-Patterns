package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 燃烧效果
 * 造成持续伤害并降低攻击力
 */
public class BurningEffect extends BaseEffect {
    private float tickInterval;    // 伤害间隔
    private float tickTimer;       // 计时器
    private float damagePerTick;   // 每次伤害量
    private float attackPenalty;   // 攻击力降低百分比
    
    public BurningEffect(float duration, float damagePerSecond, float tickInterval, float attackPenalty) {
        super(duration, damagePerSecond, EffectType.BURNING);
        this.tickInterval = tickInterval;
        this.tickTimer = 0;
        this.damagePerTick = damagePerSecond * tickInterval;
        this.attackPenalty = Math.min(1.0f, Math.max(0, attackPenalty));  // 限制在0-1之间
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
        // 添加燃烧视觉效果
        target.addVisualEffect("burning");
        
        // 播放燃烧音效
        target.playEffectSound("burning");
        
        // 应用攻击力降低
        target.modifyAttackPower(-attackPenalty);
    }
    
    @Override
    protected void onEffectEnd() {
        // 移除燃烧视觉效果
        target.removeVisualEffect("burning");
        
        // 恢复攻击力
        target.modifyAttackPower(attackPenalty);
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
        
        // 考虑目标的火焰抗性
        float fireResistance = target.getFireResistance();
        damage *= (1 - fireResistance);
        
        // 考虑其他修正因素
        damage *= target.getDamageModifier();
        
        return Math.max(1, damage);  // 确保至少造成1点伤害
    }
    
    public float getAttackPenalty() {
        return attackPenalty;
    }
} 