package com.gdx.game.effect;

/**
 * 效果优先级枚举
 * 定义效果的执行优先级
 */
public enum EffectPriority {
    HIGHEST(0),  // 最高优先级,如无敌效果
    HIGH(1),     // 高优先级,如控制效果
    NORMAL(2),   // 普通优先级,如增益效果
    LOW(3),      // 低优先级,如视觉效果
    LOWEST(4);   // 最低优先级,如环境效果

    private final int value;

    EffectPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
} 