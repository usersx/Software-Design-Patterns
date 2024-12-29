package com.gdx.game.effect;

/**
 * 效果类型枚举
 * 定义了所有可能的效果类型
 */
public enum EffectType {
    POISON("毒性", "造成持续伤害"),
    BURNING("燃烧", "造成持续伤害并降低攻击力"),
    STUN("眩晕", "无法移动和攻击"),
    SLOW("减速", "降低移动速度"),
    BUFF("增益", "提升攻击力和速度"),
    HEAL("治疗", "恢复生命值"),
    SHIELD("护盾", "减少受到的伤害"),
    INVISIBLE("隐身", "降低被发现的概率"),
    CONFUSION("混乱", "随机移动方向"),
    SILENCE("沉默", "无法使用技能");
    
    private final String name;
    private final String description;
    
    EffectType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 