package com.gdx.game.effect;

/**
 * 伤害修饰效果接口
 * 用于修改实体的伤害输出
 */
public interface DamageModifierEffect extends EntityEffect {
    /**
     * 获取伤害乘数
     * @return 伤害乘数
     */
    float getDamageMultiplier();
} 